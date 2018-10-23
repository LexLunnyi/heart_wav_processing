#include <complex>

#include "SpectrumContainer.h"

SpectrumContainer::SpectrumContainer() {
    size = 0;
    pdata = NULL;
}



SpectrumContainer::~SpectrumContainer() {
    if (pdata != NULL) free(pdata);
}




void SpectrumContainer::alloc(unsigned int size) {
    this->size = size;
    pdata = (complex<double>*)malloc(size*sizeof(complex<double>));
}



void SpectrumContainer::set(unsigned int index, unsigned int value) {
    pdata[index].imag(0.0);
    pdata[index].real((double)value);
}