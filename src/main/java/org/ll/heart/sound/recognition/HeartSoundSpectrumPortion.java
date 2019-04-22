package org.ll.heart.sound.recognition;

/**
 *
 * @author aberdnikov
 */
public class HeartSoundSpectrumPortion {
    private int num;
    private Double real;
    private Double imag;
    private Double magnitude;
    private Double frequency;

    public HeartSoundSpectrumPortion(int num, Double real, Double imag, Double frequency) {
        this.num = num;
        this.real = real;
        this.imag = imag;
        this.frequency = frequency;
        this.magnitude = Math.sqrt(real*real + imag*imag);
    }

    public int getNum() {
        return num;
    }

    public Double getReal() {
        return real;
    }

    public Double getImag() {
        return imag;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public Double getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return "HeartSoundSpectrumPortion{" + "num=" + num + ", real=" + real + ", imag=" + imag + ", magnitude=" + magnitude + ", frequency=" + frequency + '}';
    }
}
