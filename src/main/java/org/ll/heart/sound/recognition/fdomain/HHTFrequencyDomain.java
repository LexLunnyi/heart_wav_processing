package org.ll.heart.sound.recognition.fdomain;

import org.apache.commons.math3.complex.Complex;
import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class HHTFrequencyDomain implements FrequencyDomainService {
    
    @Override
    public void forward(SignalPortion portion) {
        EmpiricalModeDecomposition emd = new EmpiricalModeDecomposition(portion.getIn(), 1);
        HilbertTransform hilbert = new HilbertTransform();
        HHTPortion hhtPortion = new HHTPortion(portion.getIn());
        while(emd.hasIMF()) {
            //Get intrinsic mode function
            double[] IMF = emd.getIMF(portion);
            //Perform Hilbert transform to get frequency
            Complex[] hd = hilbert.tranform(IMF);
            //Save for the debug
            hhtPortion.setIMF(0, hd);
        }
        portion.setFreqAdd(hhtPortion);
    }

    @Override
    public void inverse(SignalPortion portion) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void features(SignalPortion portion) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void features(SignalPortion prev, SignalPortion portion) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
