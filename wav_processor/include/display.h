#ifndef WAVE_DISPLAY_H
#define WAVE_DISPLAY_H

#include <mutex>
#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 
#include <errno.h> 
#include <X11/Xlib.h> 
#include <string>


using namespace std;


class WaveDisplay {
private:
    pthread_t thread;
    Display*  dispHandle;
    Window    windowHandle; 
    int       screenHandle;
public:
    bool running;
    
    WaveDisplay();
    virtual ~WaveDisplay();
    
    bool show();
    void close();
    bool processEvents();
};

typedef WaveDisplay* PDisplay;


#endif /* WAVE_DISPLAY_H */