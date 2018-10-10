#ifndef SPECTRUMCONTAINER_H
#define SPECTRUMCONTAINER_H

#include <memory>


using namespace std;

class SpectrumContainer {
public:
    SpectrumContainer();
    virtual ~SpectrumContainer();
private:

};


typedef unique_ptr<SpectrumContainer> PSpectrumContainer;

#endif /* SPECTRUMCONTAINER_H */

