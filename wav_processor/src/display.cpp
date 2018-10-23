#include "display.h"

extern "C" void *display_thread_proc(void *arg) {
   PDisplay pDisplay = (PDisplay)arg;
   
   if (!pDisplay->show()) return NULL;
   
   while(pDisplay->running) {
       if (!pDisplay->processEvents()) break;
   }
   
   pDisplay->close();
   
   return NULL;
}



int display_error_handler(Display * disp, XErrorEvent * err) {
    printf("display_error_handler -> %u", (unsigned int)err->error_code);
}





WaveDisplay::WaveDisplay(WavData* pData) {
    running = false;
    this->pData = pData;
    timeRatio = 10;
    timeStartPosition = 0;
    
    title = "Heart WAV processor (" + 
            to_string(pData->sampleRate) + " / " + to_string(pData->align) + " / " + 
            to_string(pData->samplesCount) + ")";
    
    if (pthread_create(&thread, NULL, display_thread_proc, this)) {
        printf("Error create display thread");
    }
}





WaveDisplay::~WaveDisplay() {
    running = false;
    //wait for the thread to finish
    if(pthread_join(thread, NULL)) {
        fprintf(stderr, "Error stopping thread\n");
    }
}





bool WaveDisplay::show() {
    running = true;
    unsigned long planeMasks[1];

    //Connect to X Server
    if ((dispHandle = XOpenDisplay(NULL)) == NULL) {
        printf("Can't connect X server: %s\n", strerror(errno));
        return false;
    }
    
    //XSetErrorHandler(&display_error_handler);
    
    /*
    int defDepth = DefaultDepth(dispHandle, 0);
    XVisualInfo vInfo;
    if (!XMatchVisualInfo(dispHandle, 0, defDepth, DirectColor, &vInfo)) {
        printf("Can't get visual info\n");
        return false;
    }
     */
    
    //Create window
    screenHandle = DefaultScreen(dispHandle);
    windowHandle = XCreateSimpleWindow(dispHandle, RootWindow(dispHandle, screenHandle),
            0, 0, 1800, 900, 1,
            BlackPixel(dispHandle, screenHandle), WhitePixel(dispHandle, screenHandle));
    XStoreName(dispHandle,windowHandle, title.c_str());

    
    
    defColorMap = XDefaultColormap(dispHandle, 0);
    //defColorMap = XCreateColormap(dispHandle, windowHandle, DefaultVisual(dispHandle, screenHandle), AllocAll);
    
    /*
    unsigned int ncolors_val = NCOLORS;
    while(1) { 
        //http://math.msu.su/~vvb/2course/Borisenko/CppProjects/GWindow/xintro.html
        Status res = XAllocColorCells(dispHandle, defColorMap, false, planeMasks, /* nplanes *//*0, colors, ncolors_val);
        if (res) {
            printf("Color cells was allocated with ncolors in -> %u\n", ncolors_val);
            break;
        }
        
        ncolors_val--;
        if (0 == ncolors_val) {
            printf("ERROR: Can't allocate color cells\n");
            return false;
        }
    }
    pixelGraph = colors[PIXEL_INDEX_GRAPH];
    */
            
    createPalette();
    
    //Set event mask
    XSelectInput(dispHandle, windowHandle, ExposureMask | KeyPressMask);
    XMapWindow(dispHandle, windowHandle);

    return true;
}



bool WaveDisplay::processEvents() {
    XEvent e;
    string msg = "Hello, World!";
    GC defGC = DefaultGC(dispHandle, screenHandle);
            
    XNextEvent(dispHandle, &e);
    if (e.type == Expose) {
        //Repaint window
        draw();
    }
    if (e.type == KeyPress) {
        KeySym ks = XLookupKeysym(&(e.xkey), 0);
        if (ks == XK_q) {
            //XCloseDisplay(dispHandle);
            //Make quit
            return false;
        } else if (ks == XK_Up) {
            if (timeRatio < 2) return true;
            timeRatio -= 1;
            draw();
        } else if (ks == XK_Down) {
            timeRatio += 1;
            draw();
        } else if (ks == XK_Right) {
            if (timeStartPosition < 500) return true;
            timeStartPosition -= 500;
            draw();
        } else if (ks == XK_Left) {
            timeStartPosition += 500;
            draw();
        } else if (ks == XK_r) {
            timeRatio = 10;
            timeStartPosition = 0;
            draw();
            return true;
        } else {
            return true;
        }
    }

    return true;
}





void WaveDisplay::close() {
    //XFreeColormap(dispHandle, defColorMap);
    XCloseDisplay(dispHandle);
    running = false;
}






void WaveDisplay::showTimeDiagram(unsigned int graphWidth) {
    setColor(blackColor);
    GC defGC = DefaultGC(dispHandle, screenHandle);
    XFillRectangle(dispHandle, windowHandle, defGC, 50, 50, graphWidth, 200);
    
    setColor(whiteColor);
    unsigned int value;
    unsigned int timeIndex = 0;
    double valRatio = (double)(pData->maxValue - pData->minValue) / 200.0;
    pData->rewind(timeStartPosition);
    
    if (!pData->popSample(&value)) return;
    unsigned int firstX = 50 + timeIndex / timeRatio;
    unsigned int firstY = 250 - (unsigned int)((double)(value - pData->minValue)/valRatio);
    unsigned int secondX = 0;
    unsigned int secondY = 0;
    
    while (pData->popSample(&value)) {
        secondX = 50 + timeIndex / timeRatio;
        if (secondX > (graphWidth + 50)) break;
        secondY = 250 - (unsigned int)((double)(value - pData->minValue)/valRatio);

        XDrawLine(dispHandle, windowHandle, defGC, firstX, firstY, secondX, secondY);
        
        firstX = secondX;
        firstY = secondY;
        timeIndex++;
    }
    setColor(blackColor);
}




void WaveDisplay::setColor(XColor & color) {
    XSetForeground(dispHandle, DefaultGC(dispHandle, screenHandle), color.pixel);
}





void WaveDisplay::draw() {
    XWindowAttributes wndAttr;
    XGetWindowAttributes(dispHandle, windowHandle, &wndAttr);
    unsigned int graphWidth = (wndAttr.width > 100) ? wndAttr.width-100: wndAttr.width;
    
    GC defGC = DefaultGC(dispHandle, screenHandle);
    showTimeDiagram(graphWidth);
    showSpectrumDiagram(graphWidth);

    
    /*
    setColor(graphColors[50]);
    XFillRectangle(dispHandle, windowHandle, defGC, 50, 50, 2, 450);
    setColor(graphColors[100]);
    XFillRectangle(dispHandle, windowHandle, defGC, 450, 50, 2, 450);
    setColor(graphColors[150]);
    XFillRectangle(dispHandle, windowHandle, defGC, 850, 50, 2, 450);
    setColor(graphColors[200]);
    XFillRectangle(dispHandle, windowHandle, defGC, 1250, 50, 2, 450);
    */
    
}




void WaveDisplay::showSpectrumDiagram(unsigned int graphWidth) {
    unsigned short values[graphWidth][200];
    

    setColor(blackColor);
    GC defGC = DefaultGC(dispHandle, screenHandle);
    XFillRectangle(dispHandle, windowHandle, defGC, 50, 300, graphWidth, 200);
    
    
    unsigned int timeIndex = 0;
    pData->rewind(timeStartPosition);
    PSpectrumContainer spectrum = pData->popSpectrum();
    
    while (spectrum != NULL) {
        unsigned int X = timeIndex / timeRatio;
        if (X >= graphWidth) break;
        
        for (unsigned int Y = 0; Y < 200; Y++) {
            values[X][Y] = (unsigned short)(((double)Y / 200.0)*255.0);
            //setColor(graphColors[red]);
            //printf("Set color %u\n", red);
            //XDrawPoint(dispHandle, windowHandle, defGC, X, 500 - Y);
        }
        
        timeIndex++;
        spectrum = pData->popSpectrum();
    }
    
    unsigned short lastVal = 0;
    for(int x = 0; x < graphWidth; x++) {
        for(int y = 0; y < 200; y++) {
            if (values[x][y] != lastVal) {
                setColor(graphColors[values[x][y]]);
                lastVal = values[x][y];
            }
            XDrawPoint(dispHandle, windowHandle, defGC, 50 + x, 500 - y);
        }
    }
    
    
    
    setColor(blackColor);
}






void WaveDisplay::createPalette() {
    blackColor.red = 0; blackColor.green = 0; blackColor.blue = 0; blackColor.flags = DoRed | DoGreen | DoBlue;
    XAllocColor(dispHandle, XDefaultColormap(dispHandle, screenHandle), &blackColor);

    whiteColor.red = 0xFFFF; whiteColor.green = 0xFFFF; whiteColor.blue = 0xFFFF; whiteColor.flags = DoRed | DoGreen | DoBlue;
    XAllocColor(dispHandle, XDefaultColormap(dispHandle, screenHandle), &whiteColor);
    
    redColor.red = 0xFFFF; redColor.green = 0; redColor.blue = 0; redColor.flags = DoRed | DoGreen | DoBlue;
    XAllocColor(dispHandle, XDefaultColormap(dispHandle, screenHandle), &redColor);

    greenColor.red = 0; greenColor.green = 0xFFFF; greenColor.blue = 0; greenColor.flags = DoRed | DoGreen | DoBlue;
    XAllocColor(dispHandle, XDefaultColormap(dispHandle, screenHandle), &greenColor);

    blueColor.red = 0; blueColor.green = 0; blueColor.blue = 0xFFFF; blueColor.flags = DoRed | DoGreen | DoBlue;
    XAllocColor(dispHandle, XDefaultColormap(dispHandle, screenHandle), &blueColor);

    for (int i = 0; i < 255; i++) {
        graphColors[i].pixel = i;
        graphColors[i].flags = DoRed | DoGreen | DoBlue;
        graphColors[i].red = i * 256;
        graphColors[i].blue = 0;
        graphColors[i].green = 0;
        
        XAllocColor(dispHandle, XDefaultColormap(dispHandle, screenHandle), &graphColors[i]);
    }

    //XSetWindowColormap(dispHandle, windowHandle, defColorMap);
}