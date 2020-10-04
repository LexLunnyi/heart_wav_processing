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
    SX(6);
    
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
    
    double[] in;
    double[] out;
    Complex[] spectrum;
    
    
    public SignalPortion(int id, Date ts, double src, double[] in) {
        this.id = id;
        this.in = in;
        this.ts = ts;
        this.source = src;
        this.in = in;
        this.filtered = 0.0;
        this.magnitude = 0.0;
        this.Mfreq = 0.0;
        this.sx = false;
    }
    
    public String toCSV() {
        String SX = (sx)?"1":"0";
        return id + ";" + ts.getTime() + ";" + source + ";" + filtered + ";" + 
               magnitude + ";" + Mfreq + ";" + SX + "\n";
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
            sbuf.append(SignalColumn.M_FREQ.name()).append(";");;
            sbuf.append(SignalColumn.SX.name());
//            sbuf.append(SignalColumn.HARMONIC_INDEX.name()).append(";");
//            sbuf.append(SignalColumn.SQUARE_SEMI_WAVE.name()).append(";");
//            sbuf.append(SignalColumn.WINDOWS_ENERGY.name()).append(";");
//            sbuf.append(SignalColumn.TIME_FROM_CHANGE_POINT.name()).append(";");
//            sbuf.append(SignalColumn.TIME_FROM_INFLECTION_POINT.name()).append(";");
//            sbuf.append(SignalColumn.MAGNITUDES_ANGLE.name()).append(";");
//            sbuf.append(SignalColumn.WINDOW_CHANGE_POINTS_CNT.name()).append(";");
//            sbuf.append(SignalColumn.WINDOW_INFLECTION_POINTS_CNT.name()).append(";");
//            sbuf.append(SignalColumn.FIRST_DERIVATIVE.name()).append(";");
//            sbuf.append(SignalColumn.SECOND_DERIVATIVE.name());
        }
        return sbuf.toString();
    }
}
