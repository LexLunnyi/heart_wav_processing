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
    MFREQ(5),
    STAT_MAGNITUDE_MEAN(6),
    STAT_MAGNITUDE_SD(7),
    STAT_MFREQ_MEAN(8),
    STAT_MFREQ_SD(9),
    THRESHOLD_HISTOGRAM(10),
    SX(11);
    
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
    
    double statMagnitudeMean;
    double statMfreqMean;
    double statMagnitudeSD;
    double statMfreqSD;
    double thresholdHistogram;
    
    double[] in;
    double[] out;
    Complex[] spectrum;
    
    Object freqAdd;
    
    
    public SignalPortion(int id, Date ts, double src, double[] in) {
        this.id = id;
        this.in = in;
        this.ts = ts;
        this.source = src;
        this.in = in;
        
        filtered = 0.0;
        magnitude = 0.0;
        Mfreq = 0.0;
                
        statMagnitudeMean = 0.0;
        statMfreqMean = 0.0;
        statMagnitudeSD = 0.0;
        statMfreqSD = 0.0;
        thresholdHistogram = 0.0;
        
        sx = false;
        freqAdd = null;
    }
    
    public String toCSV() {
        String SX = (sx)?"1":"0";
        
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(id + ";");
        sbuf.append(ts.getTime() + ";");
        sbuf.append(source + ";");
        sbuf.append(filtered + ";");
        sbuf.append(magnitude + ";");
        sbuf.append(Mfreq + ";");
        sbuf.append(statMagnitudeMean + ";");
        sbuf.append(statMagnitudeSD + ";");
        sbuf.append(statMfreqMean + ";");
        sbuf.append(statMfreqSD + ";");
        sbuf.append(thresholdHistogram + ";");
        sbuf.append(SX + "\n");
        
        return sbuf.toString();
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

    public double getStatMagnitudeMean() {
        return statMagnitudeMean;
    }

    public void setStatMagnitudeMean(double statMagnitudeMean) {
        this.statMagnitudeMean = statMagnitudeMean;
    }

    public double getStatMfreqMean() {
        return statMfreqMean;
    }

    public void setStatMfreqMean(double statMfreqMean) {
        this.statMfreqMean = statMfreqMean;
    }

    public double getStatMagnitudeSD() {
        return statMagnitudeSD;
    }

    public void setStatMagnitudeSD(double statMagnitudeSD) {
        this.statMagnitudeSD = statMagnitudeSD;
    }

    public double getStatMfreqSD() {
        return statMfreqSD;
    }

    public void setStatMfreqSD(double statMfreqSD) {
        this.statMfreqSD = statMfreqSD;
    }

    public double getThresholdHistogram() {
        return thresholdHistogram;
    }

    public void setThresholdHistogram(double thresholdHistogram) {
        this.thresholdHistogram = thresholdHistogram;
    }
    
    public String getCSVColumnsNames(boolean sourceOnly) {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(SignalColumn.ID.name()).append(";");
        sbuf.append(SignalColumn.TIME.name()).append(";");
        sbuf.append(SignalColumn.SIGNAL.name());
        if (!sourceOnly) {
            sbuf.append(";").append(SignalColumn.FILTRED.name()).append(";");
            sbuf.append(SignalColumn.MAGNITUDE.name()).append(";");
            sbuf.append(SignalColumn.MFREQ.name()).append(";");
            sbuf.append(SignalColumn.STAT_MAGNITUDE_MEAN.name()).append(";");
            sbuf.append(SignalColumn.STAT_MAGNITUDE_SD.name()).append(";");
            sbuf.append(SignalColumn.STAT_MFREQ_MEAN.name()).append(";");
            sbuf.append(SignalColumn.STAT_MFREQ_SD.name()).append(";");
            sbuf.append(SignalColumn.THRESHOLD_HISTOGRAM.name()).append(";");
            sbuf.append(SignalColumn.SX.name());
        }
        return sbuf.toString();
    }

    public Object getFreqAdd() {
        return freqAdd;
    }

    public void setFreqAdd(Object freqAdd) {
        this.freqAdd = freqAdd;
    }
}