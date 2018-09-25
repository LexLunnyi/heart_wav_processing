#ifndef TESTERFFT_H
#define TESTERFFT_H

#include <math.h>
#include <stdio.h>

class TesterFFT {
private:
    static const unsigned int MAX_AMPLITUDE = 32767;
    static const unsigned int MAX_FREQUENCIES = 5;
    
    static const unsigned int SAMPLE_RATE = 11025;
    static const unsigned int SAMPLES_SIZE = 1024*10;
    
    static const double SOURCE_FREQUENCIES(int index) {
        static const double res[] = {10.0, 50.0, 100.0, 500.0, 1000.0};
        //static const double res[] = {1.0, 1.0, 1.0, 1.0, 1.0};
        return res[index];
    }
    
    static const double QUANTUM() {
        return 1.0 / ((double)SAMPLE_RATE);
    }
    
    unsigned int timeData[SAMPLES_SIZE];
    unsigned int calcSample(unsigned int index);
public:
    TesterFFT();
    virtual ~TesterFFT();
    void process();
};

#endif /* TESTERFFT_H */

