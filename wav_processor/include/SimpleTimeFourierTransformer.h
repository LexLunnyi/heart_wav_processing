#ifndef SIMPLETIMEFOURIERTRANSFORMER_H
#define SIMPLETIMEFOURIERTRANSFORMER_H

#include "SpectrumContainer.h"
#include "FastFourierTransformer.h"

#include <deque>
#include <complex>

using namespace std;

class SimpleTimeFourierTransformer {
public:
    SimpleTimeFourierTransformer();
    virtual ~SimpleTimeFourierTransformer();
    
    PSpectrumContainer quant(unsigned int value);
private:
    static const unsigned int STFT_WINDOW_SIZE = 1024;
    static const unsigned int STFT_WINDOW_STEP = 1;
    
    unsigned int quantsCount;
    
    FastFourierTransformer fft;
    
    deque<unsigned int> row;
};

#endif /* SIMPLETIMEFOURIERTRANSFORMER_H */