#include "FastFourierTransformer.h"



bool FastFourierTransformer::forward(const complex<double> * const input, complex<double> * const output, const unsigned int n) {
    if (!input || !output || n < 1 || n & (n - 1)) return false;

    rearrange(input, output, n);
    perform(output, n);

    return true;
}




bool FastFourierTransformer::forward(complex<double> * const data, const unsigned int n) {
    if (!data || n < 1 || n & (n - 1)) return false;

    rearrange(data, n);
    perform(data, n);

    return true;
}




bool FastFourierTransformer::inverse(const complex<double> * const input, complex<double> * const output, const unsigned int n, const bool need_scale /* = true */) {
    if (!input || !output || n < 1 || n & (n - 1)) return false;
    
    rearrange(input, output, n);
    perform(output, n, true);
    if (need_scale) scale(output, n);

    return true;
}



bool FastFourierTransformer::inverse(complex<double> * const data, const unsigned int n, const bool need_scale /* = true */) {
    if (!data || n < 1 || n & (n - 1)) return false;

    rearrange(data, n);
    perform(data, n, true);
    if (need_scale) scale(data, n);

    return true;
}




void FastFourierTransformer::rearrange(const complex<double> * const input, complex<double> * const output, const unsigned int n) {
    unsigned int target = 0;

    for (unsigned int pos = 0; pos < n; ++pos) {
        output[target] = input[pos];
        unsigned int mask = n;
        while (target & (mask >>= 1)) target &= ~mask;
        target |= mask;
    }
}




void FastFourierTransformer::rearrange(complex<double> * const data, const unsigned int n) {
    unsigned int target = 0;
    for (unsigned int pos = 0; pos < n; ++pos) {
        if (target > pos) {
            const complex<double> temp(data[target]);
            data[target] = data[pos];
            data[pos] = temp;
        }
        unsigned int mask = n;
        while (target & (mask >>= 1)) target &= ~mask;
        target |= mask;
    }
}




void FastFourierTransformer::perform(complex<double> * const data, const unsigned int n, const bool inverse /* = false */) {
    const double pi = inverse ? 3.14159265358979323846 : -3.14159265358979323846;
    
    for (unsigned int step = 1; step < n; step <<= 1) {
        const unsigned int jump = step << 1;
        const double delta = pi / double(step);
        const double sine = sin(delta * .5);
        const complex<double> multiplier(-2. * sine * sine, sin(delta));
        complex<double> factor(1.);
        for (unsigned int group = 0; group < step; ++group) {
            for (unsigned int pair = group; pair < n; pair += jump) {
                const unsigned int match = pair + step;
                const complex<double> product(factor * data[match]);
                data[match] = data[pair] - product;
                data[pair] += product;
            }
            factor = multiplier * factor + factor;
        }
    }
}




void FastFourierTransformer::scale(complex<double> * const data, const unsigned int n) {
    const double factor = 1. / double(n);
    for (unsigned int pos = 0; pos < n; ++pos) data[pos] *= factor;
}
