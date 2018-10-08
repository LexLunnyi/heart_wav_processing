#ifndef WAVE_DISPLAY_H
#define WAVE_DISPLAY_H

#include <mutex>
#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 
#include <errno.h> 
#include <X11/Xlib.h>
#include <X11/keysym.h>
#include <string>


#include "WavData.h"


using namespace std;


class WaveDisplay {
private:
    pthread_t thread;
    Display*  dispHandle;
    Window    windowHandle; 
    int       screenHandle;
    WavData*  pData;
    string    title;
    
    void showTimeDiagram();
public:
    bool running;
    unsigned int timeRatio;
    
    WaveDisplay(WavData* pData);
    virtual ~WaveDisplay();
    
    bool show();
    void close();
    bool processEvents();
};

typedef WaveDisplay* PDisplay;


#endif /* WAVE_DISPLAY_H */