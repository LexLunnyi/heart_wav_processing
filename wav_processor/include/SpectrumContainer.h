#ifndef SPECTRUMCONTAINER_H
#define SPECTRUMCONTAINER_H


#include <stdlib.h>
#include <complex>


using namespace std;

class SpectrumContainer {
private:
    unsigned int size;
    complex<double>* pdata;
public:
    SpectrumContainer();
    virtual ~SpectrumContainer();
    
    unsigned int getSize() {return size;}
    void alloc(unsigned int size);
    void set(unsigned int index, unsigned int value);
    complex<double>* getData() {return pdata;};
};


typedef SpectrumContainer* PSpectrumContainer;

#endif /* SPECTRUMCONTAINER_H */