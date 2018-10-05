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





WaveDisplay::WaveDisplay() {
    running = false;
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
    if ((dispHandle = XOpenDisplay(getenv("DISPLAY"))) == NULL) {
        printf("Can't connect X server: %s\n", strerror(errno));
        return false;
    }
    
    //Create window
    screenHandle = DefaultScreen(dispHandle);
    windowHandle = XCreateSimpleWindow(dispHandle, RootWindow(dispHandle, screenHandle),
            0, 0, 1000, 500, 1,
            BlackPixel(dispHandle, screenHandle), WhitePixel(dispHandle, screenHandle));
    
    //Set event mask
    XSelectInput(dispHandle, windowHandle, ExposureMask | KeyPressMask);
    XMapWindow(dispHandle, windowHandle);
    
    return true;
}




bool WaveDisplay::processEvents() {
    XEvent e;
    string msg = "Hello, World!";

    XNextEvent(dispHandle, &e);
    if (e.type == Expose) {
        //Repaint window
        XFillRectangle(dispHandle, windowHandle, DefaultGC(dispHandle, screenHandle), 20, 20, 10, 10);
        XDrawString(dispHandle, windowHandle, DefaultGC(dispHandle, screenHandle), 50, 50, msg.c_str(), msg.length());
    }
    if (e.type == KeyPress) {
        //Make quit
        return false;
    }
    
    printf("Event -> %i", e.type);
    
    
    return true;
}





void WaveDisplay::close() {
    XCloseDisplay(dispHandle);
    running = false;
}
