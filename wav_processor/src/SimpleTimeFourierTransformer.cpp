#include "SimpleTimeFourierTransformer.h"

SimpleTimeFourierTransformer::SimpleTimeFourierTransformer() {
}


SimpleTimeFourierTransformer::~SimpleTimeFourierTransformer() {
    row.clear();
}




PSpectrumContainer SimpleTimeFourierTransformer::quant(unsigned int value) {
    row.push_back(value);
    PSpectrumContainer res = new SpectrumContainer(STFT_WINDOW_SIZE);
    
    if (row.size() >= STFT_WINDOW_SIZE) {
        //Prepare buffer
        res->alloc(STFT_WINDOW_SIZE);
        for (unsigned int i = 0; i < STFT_WINDOW_SIZE; i++) {
            res->set(i, row[i]);
        }
        row.pop_front();
        fft.forward(res->getData(), STFT_WINDOW_SIZE);
        res->prepare();
    }
    
    return res;
}
