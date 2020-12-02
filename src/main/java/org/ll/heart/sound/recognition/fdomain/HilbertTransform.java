package org.ll.heart.sound.recognition.fdomain;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 *
 * @author aberdnikov
 */
public class HilbertTransform {

    final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);

    public Complex[] tranform(double[] in) {
        //1 step. Calculate the FFT of the input sequence
        Complex[] fft = transformer.transform(in, TransformType.FORWARD);
        //2 step. Take harmonics with needed coeffs
        int len = fft.length;
        for (int i = 1; i < (len / 2); i++) {
            Complex tmp = new Complex(fft[i].getReal() * 2.0, fft[i].getImaginary() * 2.0);
            fft[i] = tmp;
        }
        for (int i = (len / 2) + 2; i < len; i++) {
            fft[i] = new Complex(0.0, 0.0);
        }
        //3 step. inverse Fast-Fourier transform
        return transformer.transform(fft, TransformType.INVERSE);
    }

}
