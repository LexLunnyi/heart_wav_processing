#ifndef SPECTRUMCONTAINER_H
#define SPECTRUMCONTAINER_H


#include <stdlib.h>
#include <complex>
#include <vector>


using namespace std;

class SpectrumContainer {
private:
    static const unsigned int FREQ_STEP_COUNT = 200;
    
    unsigned int size;
    vector<double> freqs;
    double maxValue;
    
    complex<double>* pdata;
    unsigned int freqSize;
public:
    unsigned int windowSize;
    
    SpectrumContainer(unsigned int windowSize);
    virtual ~SpectrumContainer();
    
    unsigned int getSize() {return size;}
    void alloc(unsigned int size);
    void set(unsigned int index, unsigned int value);
    complex<double>* getData() {return pdata;};
    
    double getRelativeValue(unsigned int freqIndex);
    void prepare();
};


typedef SpectrumContainer* PSpectrumContainer;

#endif /* SPECTRUMCONTAINER_H */