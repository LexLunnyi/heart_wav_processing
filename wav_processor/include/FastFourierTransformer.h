#ifndef FAST_FOURIER_TRAnSFORMER
#define FAST_FOURIER_TRAnSFORMER


#include <complex>
#include <math.h>

using namespace std;


class FastFourierTransformer {
public:
    bool forward(const complex<double>* const input, complex<double>* const output, const unsigned int n);
    bool forward(complex<double>* const data, const unsigned int n);

    bool inverse(const complex<double>* const input, complex<double>* const output, const unsigned int n, const bool need_scale = true);
    bool inverse(complex<double>* const data, const unsigned int n, const bool need_scale = true);

private:
    void rearrange(const complex<double>* const input, complex<double>* const output, const unsigned int n);
    void rearrange(complex<double>* const data, const unsigned int n);

    void perform(complex<double>* const data, const unsigned int n, const bool inverse = false);
    void scale(complex<double>* const data, const unsigned int n);
};

#endif
