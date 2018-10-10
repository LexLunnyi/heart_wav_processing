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

    //Connect to X Server
    if ((dispHandle = XOpenDisplay(NULL)) == NULL) {
        printf("Can't connect X server: %s\n", strerror(errno));
        return false;
    }
    
    //Create window
    screenHandle = DefaultScreen(dispHandle);
    windowHandle = XCreateSimpleWindow(dispHandle, RootWindow(dispHandle, screenHandle),
            0, 0, 1800, 900, 1,
            BlackPixel(dispHandle, screenHandle), WhitePixel(dispHandle, screenHandle));
    XStoreName(dispHandle,windowHandle, title.c_str());
    
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
    XCloseDisplay(dispHandle);
    running = false;
}






void WaveDisplay::showTimeDiagram(unsigned int graphWidth) {
    setColor(DC_BLACK);
    GC defGC = DefaultGC(dispHandle, screenHandle);
    XFillRectangle(dispHandle, windowHandle, defGC, 50, 50, graphWidth, 200);
    
    setColor(DC_WHITE);
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
    setColor(DC_BLACK);
}




void WaveDisplay::setColor(TDrawColor color) {
    unsigned short red = 0;
    unsigned short green = 0;
    unsigned short blue = 0;
    
    switch(color) {
        case DC_RED:
            red = 0xFFFF;
            break;
        case DC_GREEN:
            green = 0xFFFF;
            break;
        case DC_BLUE:
            blue = 0xFFFF;
            break;
        case DC_WHITE:
            red = 0xFFFF;
            green = 0xFFFF;
            blue = 0xFFFF;
            break;
    }
    setColor(red, green, blue);
}




void WaveDisplay::setColor(unsigned short red, unsigned short green, unsigned short blue) {
    XColor xcolour;
    xcolour.green = green;
    xcolour.blue = blue;
    xcolour.red = red;
    xcolour.flags = DoRed | DoGreen | DoBlue;
    XAllocColor(dispHandle, XDefaultColormap(dispHandle, screenHandle), &xcolour);
    XSetForeground(dispHandle, DefaultGC(dispHandle, screenHandle), xcolour.pixel);
}





void WaveDisplay::draw() {
    XWindowAttributes wndAttr;
    XGetWindowAttributes(dispHandle, windowHandle, &wndAttr);
    unsigned int graphWidth = (wndAttr.width > 100) ? wndAttr.width-100: wndAttr.width;
    
    GC defGC = DefaultGC(dispHandle, screenHandle);
    
    showTimeDiagram(graphWidth);
    XFillRectangle(dispHandle, windowHandle, defGC, 50, 300, graphWidth, 200);

    /*
    setColor(DC_GREEN);
    XFillRectangle(dispHandle, windowHandle, defGC, 50, 50, 2, 450);
    XFillRectangle(dispHandle, windowHandle, defGC, 450, 50, 2, 450);
    XFillRectangle(dispHandle, windowHandle, defGC, 850, 50, 2, 450);
    XFillRectangle(dispHandle, windowHandle, defGC, 1250, 50, 2, 450);    
     */
}