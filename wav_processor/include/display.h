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


typedef enum DrawColor {
    DC_BLACK,
    DC_RED,
    DC_GREEN,
    DC_BLUE,
    DC_WHITE
} TDrawColor; 

class WaveDisplay {
private:
    static const unsigned int POS_TOP = 50;
    static const unsigned int POS_LEFT = 50;
    static const unsigned int POS_WIDTH = 50;
    static const unsigned int POS_HEIGHT = 50;
    
    pthread_t thread;
    Display*  dispHandle;
    Window    windowHandle; 
    int       screenHandle;
    WavData*  pData;
    string    title;
    
    void showTimeDiagram(unsigned int graphWidth);
    void setColor(TDrawColor color);
    void setColor(unsigned short red, unsigned short green, unsigned short blue);
    void draw();
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