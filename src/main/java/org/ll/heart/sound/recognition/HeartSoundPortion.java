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
    private Double magnitude;
    private Double phase;
    private Double phaseDiff;
    private Double windowEnergy;
    private final List<HeartSoundSpectrumPortion> spectrum = new ArrayList<>();

    public HeartSoundPortion(Date ts, Double in) {
        this.ts = ts;
        this.in = in;
        this.out = 0.0D;
        this.magnitude = 0.0D;
        this.phase = 0.0D;
        this.phaseDiff = 0.0D;
        this.windowEnergy = 0.0D;
    }

    public Date getTs() {
        return ts;
    }

    public Double getIn() {
        return in;
    }

    public void setOut(Double out) {
        this.out = out;
    }
    
    public Double getOut() {
        return out;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public Double getPhase() {
        return phase;
    }

    public void setPhase(Double phase) {
        this.phase = phase;
    }

    public Double getPhaseDiff() {
        return phaseDiff;
    }

    public void setPhaseDiff(Double phaseDiff) {
        this.phaseDiff = phaseDiff;
    }

    public Double getWindowEnergy() {
        return windowEnergy;
    }

    public void setWindowEnergy(Double windowEnergy) {
        this.windowEnergy = windowEnergy;
    }
    
    public List<HeartSoundSpectrumPortion> getSpectrum() {
        return spectrum;
    }

    @Override
    public String toString() {
        return "HeartSoundPortion{" + "ts=" + ts + ", in=" + in + ", out=" + out + ", spectrum=" + spectrum + '}';
    }
}
