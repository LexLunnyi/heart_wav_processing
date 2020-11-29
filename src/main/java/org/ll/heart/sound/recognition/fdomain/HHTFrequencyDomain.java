package org.ll.heart.sound.recognition.fdomain;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class HHTFrequencyDomain implements FrequencyDomainService {
    


    @Override
    public void forward(SignalPortion portion) {
        EmpiricalModeDecomposition emd = new EmpiricalModeDecomposition(portion.getIn(), 10);
        while(emd.hasIMF()) {
            //Get intrinsic mode function
            double[] IMF = emd.getIMF(portion);
            //Perform Hilbert transform to get frequency
        }
        

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
