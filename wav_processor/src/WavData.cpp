#include <vector>

#include "WavData.h"


WavData::WavData() {
    minValue = 0;
    maxValue = 0;
    curPosition = 0;
}


WavData::~WavData() {
    samples.clear();
    spectrums.clear();
}


void WavData::pushSample(unsigned int value) {
    if (0 == samples.size()) {
        minValue = value;
        maxValue = value;
    } else {
        if (value > maxValue) maxValue = value;
        if (value < minValue) minValue = value;
    }
    
    samples.push_back(value);
}




void WavData::pushSpectrum(PSpectrumContainer value) {
    spectrums.pop_back(value);
}





bool WavData::popSample(unsigned int* pValue) {
    if (curPosition >= samples.size()) {
        return false;
    } else {
        *pValue = samples[curPosition];
        curPosition++;
        return true;
    }
}





void WavData::rewind(unsigned int newPosition) {
    if (newPosition >= samples.size()-1) return;
    curPosition = newPosition;
}