package org.ll.heart.sound.recognition.fdomain;

import org.ll.heart.sound.recognition.utils.EmpiricalModeDecomposition;
import org.ll.heart.sound.recognition.utils.HilbertTransform;
import org.apache.commons.math3.complex.Complex;
import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class HHTFrequencyDomain implements FrequencyDomainService {
    final int MAX_IMF_COUNT = 10;
    
    @Override
    public void forward(SignalPortion portion) {
        EmpiricalModeDecomposition emd = new EmpiricalModeDecomposition(portion.getIn(), MAX_IMF_COUNT);
        HilbertTransform hilbert = new HilbertTransform();
        HHTPortion hhtPortion = new HHTPortion(portion.getIn(), MAX_IMF_COUNT);
        int curIMFindex = 0;
        while(emd.hasIMF()) {
            //Get intrinsic mode function
            double[] IMF = emd.getIMF(portion);
            //Perform Hilbert transform to get frequency
            Complex[] hd = hilbert.tranform(IMF);
            //Save for the debug
            hhtPortion.setIMF(curIMFindex, hd);
            curIMFindex++;
        }
        hhtPortion.calcFeatures();
        portion.setFreqAdd(hhtPortion);
    }

    @Override
    public void inverse(SignalPortion portion) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void features(SignalPortion portion) {
        HHTPortion hht = (HHTPortion)portion.getFreqAdd();
        portion.setMagnitude(hht.getMagnitude());
        portion.setMfreq(hht.getMFreq());
    }

    @Override
    public void features(SignalPortion prev, SignalPortion portion) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
