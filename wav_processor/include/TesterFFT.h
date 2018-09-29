#ifndef TESTERFFT_H
#define TESTERFFT_H

#include <math.h>
#include <stdio.h>
#include <complex>

#include "FastFourierTransformer.h"


//graph   250 - 2750
//graph2  300 - 3900

using namespace std;

class TesterFFT {
private:
    static const unsigned int MAX_AMPLITUDE = 32767;
    static const unsigned int MAX_FREQUENCIES = 5;
    
    static const unsigned int SAMPLE_RATE = 11025;
    static const unsigned int SAMPLES_SIZE = 16384;
    
    static const double SOURCE_FREQUENCIES(int index) {
        static const double res[] = {10.0, 20.0, 30.0, 40.0, 50.0};
        //static const double res[] = {1.0, 1.0, 1.0, 1.0, 1.0};
        return res[index];
    }
    
    static const double TIME_RESOLUTION() {
        return 1.0 / ((double)SAMPLE_RATE);
    }
    
    static const double FREQ_RESOLUTION() {
        return (double)SAMPLE_RATE / ((double)SAMPLES_SIZE);
    }
    
    complex<double> timeData[SAMPLES_SIZE];
    complex<double> freqData[SAMPLES_SIZE];
    unsigned int calcSample(unsigned int index);
public:
    TesterFFT();
    virtual ~TesterFFT();
    void process();
};

#endif /* TESTERFFT_H */