#include "TesterFFT.h"

TesterFFT::TesterFFT() {
}

TesterFFT::~TesterFFT() {
}

unsigned int TesterFFT::calcSample(unsigned int index) {
    double res = (double)MAX_AMPLITUDE;
    double amp = MAX_AMPLITUDE / (MAX_FREQUENCIES);
    
    for(unsigned int i = 0; i < MAX_FREQUENCIES; i++) {
        res += amp*sin(SOURCE_FREQUENCIES(i) * 2.0*M_PI * (double)index * QUANTUM());
    }
    
    return (unsigned int)res;
}




void TesterFFT::process() {
    unsigned int index = 0;
    
    for (index = 0; index < SAMPLES_SIZE; index++) {
        timeData[index] = calcSample(index);
        printf("%u;%u\n", index, timeData[index]);
    }
}