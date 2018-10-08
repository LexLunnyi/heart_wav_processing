#ifndef WAVDATA_H
#define WAVDATA_H

#include <vector>


using namespace std;

class WavData {
public:
    //Sampling frequency. 8000 Hz, 44100 Hz and etc.
    unsigned int sampleRate;
    //Number of byte per sample (blockAlign/numChannels)
    unsigned short align;
    //Number of samples (subchunkDataSize / blockAlign )
    unsigned int samplesCount;
    
    unsigned int minValue;
    unsigned int maxValue;
    
    WavData();
    virtual ~WavData();
    
    void pushSample(unsigned int value);
    bool popSample(unsigned int* pValue);
    void rewind();
private:
    vector<unsigned int> samples;
    unsigned int curPosition;
};

#endif /* WAVDATA_H */

