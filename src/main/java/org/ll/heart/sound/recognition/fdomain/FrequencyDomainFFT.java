package org.ll.heart.sound.recognition.fdomain;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class FrequencyDomainFFT implements FrequencyDomainService {

    private final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);

    public FrequencyDomainFFT() {
    }

    @Override
    public void forward(SignalPortion portion) {
        portion.setSpectrum(transformer.transform(portion.getIn(), TransformType.FORWARD));
    }

    @Override
    public void inverse(SignalPortion portion) {
        Complex[] s = portion.getSpectrum();
        Complex[] out = transformer.transform(s, TransformType.INVERSE);
        Complex val = out[out.length / 2];
        if (val.getReal() < 0) {
            portion.setFiltered(val.abs() * (-1.0));
        } else {
            portion.setFiltered(val.abs());
        }
        Complex m = new Complex(0);
        for(Complex g : s) {
            m = m.add(g);
        }
        portion.setMagnitude(m.abs());
    }

    @Override
    public void forward(SignalPortion prev, SignalPortion portion) {
        ///return portion;
    }
}