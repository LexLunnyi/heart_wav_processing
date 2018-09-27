#ifndef FAST_FOURIER_TRANSFORMER
#define FAST_FOURIER_TRANSFORMER

//Cooley–Tukey FFT algorithm
//https://en.wikipedia.org/wiki/Cooley%E2%80%93Tukey_FFT_algorithm


#include <complex>
#include <math.h>



using namespace std;

class FastFourierTransformer {
private:
    void separate(complex<double>* a, int n);
public:
    FastFourierTransformer() {}
    virtual ~FastFourierTransformer() {}
    
    void fft2(complex<double>* X, int N);
};


#endif
