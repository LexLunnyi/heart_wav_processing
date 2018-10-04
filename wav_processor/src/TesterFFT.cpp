#include "TesterFFT.h"

TesterFFT::TesterFFT() {
    
}

TesterFFT::~TesterFFT() {
}

unsigned int TesterFFT::calcSample(unsigned int index) {
    double res = (double)MAX_AMPLITUDE;
    double amp = MAX_AMPLITUDE / (MAX_FREQUENCIES);
    
    for(unsigned int i = 0; i < MAX_FREQUENCIES; i++) {
        res += amp*sin(SOURCE_FREQUENCIES(i) * 2.0*M_PI * (double)index * TIME_RESOLUTION());
    }
    
    return (unsigned int)res;
}




void TesterFFT::process() {
    unsigned int index = 0;
    
    for (index = 0; index < SAMPLES_SIZE; index++) {
        timeData[index] = complex<double>(0.,0.);
        timeData[index] += calcSample(index);
        freqData[index] = timeData[index];
        //printf("%u;%u\n", index, timeData[index]);
    }
    
    FastFourierTransformer fft;
    fft.forward(&freqData[0], SAMPLES_SIZE);
    
    double curTime = 0.0;
    double curFreq = 0.0;
    for (index = 0; index < SAMPLES_SIZE; index++) {
        curTime = index*TIME_RESOLUTION();
        curFreq = index*FREQ_RESOLUTION();
        
        //index; time; timeVal; freq; freqVal
        printf("%u;%.3f;%.3f;%.3f;%.3f\n", index, curTime, timeData[index].real(), curFreq, abs(freqData[index]));
    }
}