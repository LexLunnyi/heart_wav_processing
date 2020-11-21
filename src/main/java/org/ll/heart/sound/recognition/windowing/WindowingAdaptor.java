package org.ll.heart.sound.recognition.windowing;

import org.apache.commons.math3.complex.Complex;

/**
 * @author aberdnikov
 */
public class WindowingAdaptor {
    WindowingProcessor getProcessor(WindowingType type) {
        
        switch (type) {
            case SIN:
                return new WProcessorSin();
            case BARTLETT:
                return new WProcessorBarlett();
            case LANCZOS:
                return new WProcessorLanczos();
            case HANN:
                return new WProcessorHann();
            case HAMMING:
                return new WProcessorHamming();
            case BARTLETT_HANN:
                return new WProcessorBarlettHann();
        }
        
        return new WProcessorRectangle();
    }
    
    public Complex[] process(Complex[] in, WindowingType type) {
        return getProcessor(type).process(in);
    }
}
