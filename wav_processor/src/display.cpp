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
        showTimeDiagram();
        XFillRectangle(dispHandle, windowHandle, defGC, 50, 300, 1600, 200);

        //XFillRectangle(dispHandle, windowHandle, defGC, 50, 50, 2, 450);
        //XFillRectangle(dispHandle, windowHandle, defGC, 250, 50, 2, 450);
        //XFillRectangle(dispHandle, windowHandle, defGC, 450, 50, 2, 450);
        //XFillRectangle(dispHandle, windowHandle, defGC, 650, 50, 2, 450);
    }
    if (e.type == KeyPress) {
        KeySym ks = XLookupKeysym(&(e.xkey), 0);
        if (ks == XK_q) {
            //Make quit
            return false;
        } else {
            return true;
        }
    }
    
    //printf("Event (N) -> %i", e.type);
    
    
    return true;
}





void WaveDisplay::close() {
    XCloseDisplay(dispHandle);
    running = false;
}






void WaveDisplay::showTimeDiagram() {
    GC defGC = DefaultGC(dispHandle, screenHandle);
    XFillRectangle(dispHandle, windowHandle, defGC, 50, 50, 1600, 200);
    
    XColor xcolour;
    Colormap cmap= XDefaultColormap(dispHandle, screenHandle);
    xcolour.green = 65535;
    xcolour.blue = 65535;
    xcolour.red = 65535;
    xcolour.flags = DoRed | DoGreen | DoBlue;
    XAllocColor(dispHandle, cmap, &xcolour);
    XSetForeground(dispHandle, defGC, xcolour.pixel);
    
    unsigned int value;
    unsigned int timeIndex = 0;
    double valRatio = (double)(pData->maxValue - pData->minValue) / 200.0;
    pData->rewind();
    
    while (pData->popSample(&value)) {
        unsigned int X = 50 + timeIndex / timeRatio;
        if (X > 1650) break;
        unsigned int Y = 250 - (unsigned int)((double)(value - pData->minValue)/valRatio);

        XDrawPoint(dispHandle, windowHandle, defGC, X, Y);
        timeIndex++;
    }
    
    
}