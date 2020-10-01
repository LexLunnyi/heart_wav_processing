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
    FILTERED(2);
    
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
    double[] in;
    double[] out;
    Complex[] spectrum;
    
    
    public SignalPortion(Date ts, double src, double[] in) {
        this.in = in;
        this.ts = ts;
        this.source = src;
        this.in = in;
    }
    
    public String toCSV() {
        return ts.getTime() + ";" + source + ";" + filtered + "\n";
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
    
    public String getCSVColumnsNames(boolean sourceOnly) {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(WavColumn.TIME.name()).append(";");
        sbuf.append(WavColumn.SIGNAL.name());
        if (!sourceOnly) {
            sbuf.append(";").append(WavColumn.FILTRED.name());
//            sbuf.append(WavColumn.MAGNITUDE.name()).append(";");
//            sbuf.append(WavColumn.SX.name()).append(";");
//            sbuf.append(WavColumn.HARMONIC_INDEX.name()).append(";");
//            sbuf.append(WavColumn.SQUARE_SEMI_WAVE.name()).append(";");
//            sbuf.append(WavColumn.WINDOWS_ENERGY.name()).append(";");
//            sbuf.append(WavColumn.TIME_FROM_CHANGE_POINT.name()).append(";");
//            sbuf.append(WavColumn.TIME_FROM_INFLECTION_POINT.name()).append(";");
//            sbuf.append(WavColumn.MAGNITUDES_ANGLE.name()).append(";");
//            sbuf.append(WavColumn.WINDOW_CHANGE_POINTS_CNT.name()).append(";");
//            sbuf.append(WavColumn.WINDOW_INFLECTION_POINTS_CNT.name()).append(";");
//            sbuf.append(WavColumn.FIRST_DERIVATIVE.name()).append(";");
//            sbuf.append(WavColumn.SECOND_DERIVATIVE.name());
        }
        return sbuf.toString();
    }
}
