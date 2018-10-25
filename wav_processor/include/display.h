#ifndef WAVE_DISPLAY_H
#define WAVE_DISPLAY_H

#include <mutex>
#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 
#include <errno.h> 
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <X11/Xos.h>
#include <X11/keysym.h>
#include <string>


#include "WavData.h"


using namespace std;


int display_error_handler(Display * disp, XErrorEvent * err);

class WaveDisplay {
private:
    static const unsigned int POS_TOP = 50;
    static const unsigned int POS_LEFT = 50;
    static const unsigned int POS_WIDTH = 50;
    static const unsigned int POS_HEIGHT = 50;

    static const unsigned int NCOLORS = 3;
    static const unsigned int PIXEL_INDEX_GRAPH = 0;
    
    static constexpr double FILTER_BORDER_FREQ = 150.0;
    
    unsigned long colors[NCOLORS];
    
    XColor graphColors[255];
    XColor redColor;
    XColor blueColor;
    XColor greenColor;
    XColor whiteColor;
    XColor blackColor;
    
    pthread_t thread;
    Display*  dispHandle;
    Window    windowHandle; 
    int       screenHandle;
    WavData*  pData;
    string    title;
    Colormap  defColorMap;
    
    unsigned long pixelGraph;
    
    unsigned int timeStartPosition;
    
    void showTimeDiagram(unsigned int graphWidth);
    void showSpectrumDiagram(unsigned int graphWidth);
    void setColor(XColor & color);
    void draw();
    void createPalette();
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
