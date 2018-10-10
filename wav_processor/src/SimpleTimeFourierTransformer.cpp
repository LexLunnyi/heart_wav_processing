#include "SimpleTimeFourierTransformer.h"

SimpleTimeFourierTransformer::SimpleTimeFourierTransformer() {
    quantsCount = 0;
}


SimpleTimeFourierTransformer::~SimpleTimeFourierTransformer() {
}




PSpectrumContainer SimpleTimeFourierTransformer::quant(unsigned int value) {
    quantsCount++;
    
    if (quantsCount > STFT_WINDOW_SIZE);
}
