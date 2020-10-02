package org.ll.heart.sound.recognition;

import java.util.Date;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author aberdnikov
 */


enum SignalColumn {
    TIME(0),
    SIGNAL(1),
    FILTRED(2),
    MAGNITUDE(3);
    
    int index;

    private SignalColumn(int index) {
        this.index = index;
    }
    
    public int get() {
        return this.index;
    }
}



public class SignalPortion {
    Date ts;
    double source;
    double filtered;
    double magnitude;
    double[] in;
    double[] out;
    Complex[] spectrum;
    
    
    public SignalPortion(Date ts, double src, double[] in) {
        this.in = in;
        this.ts = ts;
        this.source = src;
        this.in = in;
        this.filtered = 0.0;
        this.magnitude = 0.0;
    }
    
    public String toCSV() {
        return ts.getTime() + ";" + source + ";" + filtered + ";" + magnitude + "\n";
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
    
    public String getCSVColumnsNames(boolean sourceOnly) {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(SignalColumn.TIME.name()).append(";");
        sbuf.append(SignalColumn.SIGNAL.name());
        if (!sourceOnly) {
            sbuf.append(";").append(SignalColumn.FILTRED.name()).append(";");
            sbuf.append(SignalColumn.MAGNITUDE.name());
//            sbuf.append(SignalColumn.SX.name()).append(";");
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
