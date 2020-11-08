package org.ll.heart.sound.recognition;

import java.util.Date;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author aberdnikov
 */


enum SignalColumn {
    ID(0),
    TIME(1),
    SIGNAL(2),
    FILTRED(3),
    MAGNITUDE(4),
    M_FREQ(5),
    STAT_MEAN(6),
    STAT_SD(7),
    SX(8);
    
    int index;

    private SignalColumn(int index) {
        this.index = index;
    }
    
    public int get() {
        return this.index;
    }
}



public class SignalPortion {
    int id;
    Date ts;
    double source;
    double filtered;
    double magnitude;
    double Mfreq;
    boolean sx;
    double magnitudeMean;
    double mfreqMean;
    double magnitudeSD;
    double mfreqSD;
    
    double[] in;
    double[] out;
    Complex[] spectrum;
    
    
    public SignalPortion(int id, Date ts, double src, double[] in) {
        this.id = id;
        this.in = in;
        this.ts = ts;
        this.source = src;
        this.in = in;
        
        filtered = 0.0;
        magnitude = 0.0;
        Mfreq = 0.0;
        sx = false;
        magnitudeMean = 0.0;
        mfreqMean = 0.0;
        magnitudeSD = 0.0;
        mfreqSD = 0.0;
    }
    
    public String toCSV() {
        String SX = (sx)?"1":"0";
        return id + ";" + ts.getTime() + ";" + source + ";" + filtered + ";" + 
               magnitude + ";" + Mfreq + ";" + magnitudeMean + ";" + 
               magnitudeSD + ";" + SX + "\n";
    }

    public double[] getIn() {
        return in;
    }

    public Complex[] getSpectrum() {
        return spectrum;
    }

    public void setSpectrum(Complex[] spectrum) {
        this.spectrum = spectrum;
    }

    public void setFiltered(double filtered) {
        this.filtered = filtered;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public double getSource() {
        return source;
    }

    public double getFiltered() {
        return filtered;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setSource(double source) {
        this.source = source;
    }

    public double getMfreq() {
        return Mfreq;
    }

    public void setMfreq(double Mfreq) {
        this.Mfreq = Mfreq;
    }

    public boolean isSx() {
        return sx;
    }

    public void setSx(boolean sx) {
        this.sx = sx;
    }
    
    public String getCSVColumnsNames(boolean sourceOnly) {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(SignalColumn.ID.name()).append(";");
        sbuf.append(SignalColumn.TIME.name()).append(";");
        sbuf.append(SignalColumn.SIGNAL.name());
        if (!sourceOnly) {
            sbuf.append(";").append(SignalColumn.FILTRED.name()).append(";");
            sbuf.append(SignalColumn.MAGNITUDE.name()).append(";");
            sbuf.append(SignalColumn.M_FREQ.name()).append(";");
            sbuf.append(SignalColumn.STAT_MEAN.name()).append(";");
            sbuf.append(SignalColumn.STAT_SD.name()).append(";");
            sbuf.append(SignalColumn.SX.name());
        }
        return sbuf.toString();
    }
}