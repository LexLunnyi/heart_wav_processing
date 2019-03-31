package org.ll.heart.sound.recognition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aberdnikov
 */
public class HeartSoundPortion {
    private Date ts;
    private Double in;
    private Double out;
    private final List<HeartSoundSpectrumPortion> spectrum = new ArrayList<>();

    public HeartSoundPortion(Date ts, Double in) {
        this.ts = ts;
        this.in = in;
    }

    public Date getTs() {
        return ts;
    }

    public Double getIn() {
        return in;
    }

    public Double getOut() {
        return out;
    }

    public List<HeartSoundSpectrumPortion> getSpectrum() {
        return spectrum;
    }

    @Override
    public String toString() {
        return "HeartSoundPortion{" + "ts=" + ts + ", in=" + in + ", out=" + out + ", spectrum=" + spectrum + '}';
    }
}
