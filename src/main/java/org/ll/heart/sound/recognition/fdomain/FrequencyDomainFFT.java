package org.ll.heart.sound.recognition.fdomain;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class FrequencyDomainFFT implements FrequencyDomainService {
    private final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.UNITARY);
    
    public FrequencyDomainFFT() {
    }

    @Override
    public void process(SignalPortion portion) {
        portion.setSpectrum(transformer.transform(portion.getIn(), TransformType.FORWARD));
    }

    @Override
    public void process(SignalPortion prev, SignalPortion portion) {
        ///return portion;
    }
}
