#include <complex>
#include <vector>

#include "SpectrumContainer.h"

SpectrumContainer::SpectrumContainer(unsigned int windowSize) {
    maxValue = 0;
    size = 0;
    freqSize = 0;
    pdata = NULL;
    this->windowSize = windowSize;
}



SpectrumContainer::~SpectrumContainer() {
    if (pdata != NULL) free(pdata);
    freqs.clear();
}




void SpectrumContainer::alloc(unsigned int size) {
    this->size = size;
    pdata = (complex<double>*)malloc(size*sizeof(complex<double>));
}



void SpectrumContainer::set(unsigned int index, unsigned int value) {
    pdata[index].imag(0.0);
    pdata[index].real((double)value);
}





void SpectrumContainer::prepare() {
    unsigned int count = size / 2 + 1;
    freqSize = (count > (FREQ_STEP_COUNT+1)) ? FREQ_STEP_COUNT+1 : count;
    
    for (unsigned int i = 1; i <= freqSize; i++) {
        double curVal = abs(pdata[i]);
        freqs.push_back(curVal);
        
        if (curVal > maxValue) maxValue = curVal;
        //fullSum += curVal;
    }
}





double SpectrumContainer::getRelativeValue(unsigned int freqIndex) {
    if (freqIndex > freqSize) return 0;
    
    return freqs[freqIndex] / maxValue;
}