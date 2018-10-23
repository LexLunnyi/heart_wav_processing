#include "SimpleTimeFourierTransformer.h"

SimpleTimeFourierTransformer::SimpleTimeFourierTransformer() {
    quantsCount = 0;
}


SimpleTimeFourierTransformer::~SimpleTimeFourierTransformer() {
}




PSpectrumContainer SimpleTimeFourierTransformer::quant(unsigned int value) {
    quantsCount++;
    row.push_back(value);
    PSpectrumContainer res = new SpectrumContainer();
    
    if (row.size() >= STFT_WINDOW_SIZE) {
        //Prepare buffer
        res->alloc(STFT_WINDOW_SIZE);
        for (unsigned int i = 0; i < STFT_WINDOW_SIZE; i++) {
            res->set(i, row[i]);
        }
        row.pop_front();
        fft.forward(res->getData(), STFT_WINDOW_SIZE);
    }
    
    return res;
}
