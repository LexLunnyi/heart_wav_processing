package org.ll.heart.sound.recognition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aberdnikov
 */



enum WavColumn {
    TIME(0),
    SIGNAL(1),
    FILTRED(2),
    MAGNITUDE(3),
    SX(4),
    HARMONIC_INDEX(5),
    SQUARE_SEMI_WAVE(6),
    WINDOWS_ENERGY(7),
    TIME_FROM_CHANGE_POINT(8),
    TIME_FROM_INFLECTION_POINT(9),
    MAGNITUDES_ANGLE(10),
    WINDOW_CHANGE_POINTS_CNT(11),
    WINDOW_INFLECTION_POINTS_CNT(12),
    FIRST_DERIVATIVE(13),
    SECOND_DERIVATIVE(14),
    SIZE(15);
    
    
    int index;

    private WavColumn(int index) {
        this.index = index;
    }
    
    public int get() {
        return this.index;
    }
}


public class HeartSoundPortion {
    private Date ts;
    private Double in;
    private Double out;
    private Double magnitude;
    private Double phase;
    private Double windowEnergy;
    private Double harmonicIndex;
    private Double squareSemiWave;
    private Double timeFromChangeDirPoint;
    private Double timeFromInflectionPoint;
    private Double magnitudesAngle;
    private Double firstDerivative;
    private Double secondDerivative;
    private Double windowChangeDirPointsCnt;
    private Double windowInflectionPointsCnt;
    private boolean Sx;
    private boolean S1;
    private boolean S2;
    private boolean changeDirectionPoint;
    private boolean inflectionPoint;
    
    static private double maxWindowEnergy = Double.MIN_VALUE;
    static private double maxMagnitude = Double.MIN_VALUE;
    static private double maxHarmonicIndex = Double.MIN_VALUE;
    static private double maxSquareSemiWave = Double.MIN_VALUE;
    static private double maxFirstDerivative = Double.MIN_VALUE;
    static private double maxSecondDerivative = Double.MIN_VALUE;
    static private double maxWindowChangeDirPointsCnt = Double.MIN_VALUE;
    static private double maxWindowInflectionPointsCnt = Double.MIN_VALUE;
    static private double maxTimeFromChangeDirPoint = Double.MIN_VALUE;
    static private double maxTimeFromInflectionPoint = Double.MIN_VALUE;
    
    private final List<HeartSoundSpectrumPortion> spectrum = new ArrayList<>();

    public HeartSoundPortion(Date ts, Double in) {
        this.ts = ts;
        this.in = in;
        this.out = Double.MIN_VALUE;
        this.magnitude = Double.MIN_VALUE;
        this.phase = Double.MIN_VALUE;
        this.windowEnergy = Double.MIN_VALUE;
        this.harmonicIndex = Double.MIN_VALUE;
        this.squareSemiWave = Double.MIN_VALUE;
        this.timeFromChangeDirPoint = Double.MIN_VALUE;
        this.timeFromInflectionPoint = Double.MIN_VALUE;
        this.magnitudesAngle = Double.MIN_VALUE;
        this.firstDerivative = Double.MIN_VALUE;
        this.secondDerivative = Double.MIN_VALUE;
        this.windowChangeDirPointsCnt = Double.MIN_VALUE;
        this.windowInflectionPointsCnt = Double.MIN_VALUE;
        this.Sx = false;
        this.S1 = false;
        this.S2 = false;
        this.changeDirectionPoint = false;
        this.inflectionPoint = false;
    }
    
    static public void init() {
        maxWindowEnergy = Double.MIN_VALUE;
        maxMagnitude = Double.MIN_VALUE;
        maxHarmonicIndex = Double.MIN_VALUE;
        maxSquareSemiWave = Double.MIN_VALUE;
        maxFirstDerivative = Double.MIN_VALUE;
        maxSecondDerivative = Double.MIN_VALUE; 
        maxWindowChangeDirPointsCnt = Double.MIN_VALUE;
        maxWindowInflectionPointsCnt = Double.MIN_VALUE;
        maxTimeFromChangeDirPoint = Double.MIN_VALUE;
        maxTimeFromInflectionPoint = Double.MIN_VALUE;
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
        if (magnitude > maxMagnitude) {
            maxMagnitude = magnitude;
        }
    }

    public Double getPhase() {
        return phase;
    }

    public void setPhase(Double phase) {
        this.phase = phase;
    }
    
    public Double getWindowEnergy() {
        return windowEnergy;
    }

    public void setWindowEnergy(Double windowEnergy) {
        this.windowEnergy = windowEnergy;
        if (windowEnergy > maxWindowEnergy) {
            maxWindowEnergy = windowEnergy;
        }
    }
    
    public List<HeartSoundSpectrumPortion> getSpectrum() {
        return spectrum;
    }

    public boolean isSx() {
        return Sx;
    }

    public void setSx(boolean Sx) {
        this.Sx = Sx;
    }

    public boolean isS1() {
        return S1;
    }

    public void setS1(boolean S1) {
        this.S1 = S1;
    }

    public boolean isS2() {
        return S2;
    }

    public void setS2(boolean S2) {
        this.S2 = S2;
    }

    public Double getHarmonicIndex() {
        return harmonicIndex;
    }

    public void setHarmonicIndex(Double harmonicIndex) {
        this.harmonicIndex = harmonicIndex;
        if (harmonicIndex > maxHarmonicIndex) {
            maxHarmonicIndex = harmonicIndex;
        }
    }

    public Double getSquareSemiWave() {
        return squareSemiWave;
    }

    public void setSquareSemiWave(Double squareSemiWave) {
        this.squareSemiWave = squareSemiWave;
        if (squareSemiWave > maxSquareSemiWave) {
            maxSquareSemiWave = squareSemiWave;
        }
    }

    public Double getTimeFromChangeDirPoint() {
        return timeFromChangeDirPoint;
    }

    public void setTimeFromChangeDirPoint(Double timeFromChangeDirPoint) {
        this.timeFromChangeDirPoint = timeFromChangeDirPoint;
        if (timeFromChangeDirPoint > maxTimeFromChangeDirPoint) {
            maxTimeFromChangeDirPoint = timeFromChangeDirPoint;
        }
    }

    public Double getTimeFromInflectionPoint() {
        return timeFromInflectionPoint;
    }

    public void setTimeFromInflectionPoint(Double timeFromInflectionPoint) {
        this.timeFromInflectionPoint = timeFromInflectionPoint;
        if (timeFromInflectionPoint > maxTimeFromInflectionPoint) {
            maxTimeFromInflectionPoint = timeFromInflectionPoint;
        }
    }

    public Double getMagnitudesAngle() {
        return magnitudesAngle;
    }

    public void setMagnitudesAngle(Double magnitudesAngle) {
        this.magnitudesAngle = magnitudesAngle;
    }

    public Double getFirstDerivative() {
        return firstDerivative;
    }

    public void setFirstDerivative(Double firstDerivative) {
        this.firstDerivative = firstDerivative;
        if (firstDerivative > maxFirstDerivative) {
            maxFirstDerivative = firstDerivative;
        }
    }

    public Double getSecondDerivative() {
        return secondDerivative;
    }

    public void setSecondDerivative(Double secondDerivative) {
        this.secondDerivative = secondDerivative;
        if (secondDerivative > maxSecondDerivative) {
            maxSecondDerivative = secondDerivative;
        }
    }

    public Double getWindowChangeDirPointsCnt() {
        return windowChangeDirPointsCnt;
    }

    public void setWindowChangeDirPointsCnt(Double windowChangeDirPointsCnt) {
        this.windowChangeDirPointsCnt = windowChangeDirPointsCnt;
        if (windowChangeDirPointsCnt > maxWindowChangeDirPointsCnt) {
            maxWindowChangeDirPointsCnt = windowChangeDirPointsCnt;
        }
    }

    public Double getWindowInflectionPointsCnt() {
        return windowInflectionPointsCnt;
    }

    public void setWindowInflectionPointsCnt(Double windowInflectionPointsCnt) {
        this.windowInflectionPointsCnt = windowInflectionPointsCnt;
        if (windowInflectionPointsCnt > maxWindowInflectionPointsCnt) {
            maxWindowInflectionPointsCnt = windowInflectionPointsCnt;
        }
    }

    public boolean isChangeDirectionPoint() {
        return changeDirectionPoint;
    }

    public void setChangeDirectionPoint(boolean changeDirectionPoint) {
        this.changeDirectionPoint = changeDirectionPoint;
    }

    public boolean isInflectionPoint() {
        return inflectionPoint;
    }

    public void setInflectionPoint(boolean inflectionPoint) {
        this.inflectionPoint = inflectionPoint;
    }
    
    
    public String getColumnsNames(boolean sourceOnly) {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(WavColumn.TIME.name()).append(";");
        sbuf.append(WavColumn.SIGNAL.name());
        if (!sourceOnly) {
            sbuf.append(WavColumn.FILTRED.name()).append(";");
            sbuf.append(WavColumn.MAGNITUDE.name()).append(";");
            sbuf.append(WavColumn.SX.name()).append(";");
            sbuf.append(WavColumn.HARMONIC_INDEX.name()).append(";");
            sbuf.append(WavColumn.SQUARE_SEMI_WAVE.name()).append(";");
            sbuf.append(WavColumn.WINDOWS_ENERGY.name()).append(";");
            sbuf.append(WavColumn.TIME_FROM_CHANGE_POINT.name()).append(";");
            sbuf.append(WavColumn.TIME_FROM_INFLECTION_POINT.name()).append(";");
            sbuf.append(WavColumn.MAGNITUDES_ANGLE.name()).append(";");
            sbuf.append(WavColumn.WINDOW_CHANGE_POINTS_CNT.name()).append(";");
            sbuf.append(WavColumn.WINDOW_INFLECTION_POINTS_CNT.name()).append(";");
            sbuf.append(WavColumn.FIRST_DERIVATIVE.name()).append(";");
            sbuf.append(WavColumn.SECOND_DERIVATIVE.name());
        }
        return sbuf.toString();
    }
    
    
    private Double[] getSourceOnlyColumns() {
        Double[] res = new Double[2];
        res[WavColumn.TIME.get()] = (double)getTs().getTime();
        res[WavColumn.SIGNAL.get()] = getIn();
        return res;
    }
    
    
    public Double[] getColumns(boolean sourceOnly) {
        if (sourceOnly) {
            return getSourceOnlyColumns();
        }
        Double[] res = new Double[WavColumn.SIZE.get()];
        res[WavColumn.TIME.get()] = (double)getTs().getTime();
        res[WavColumn.SIGNAL.get()] = getIn();
        res[WavColumn.FILTRED.get()] = getOut();
        res[WavColumn.MAGNITUDE.get()] = getMagnitude();
        res[WavColumn.SX.get()] = (isSx()) ? 1.0D : Double.MIN_VALUE;
        //double diff = (isSx()) ? 1.0 - getMagnitude() : getMagnitude();
        //diff = Math.pow(diff, 4.0);
        //res[WavColumn.SX.get()] = (isSx()) ? 1.0 - diff : diff;
        res[WavColumn.HARMONIC_INDEX.get()] = getHarmonicIndex();
        res[WavColumn.SQUARE_SEMI_WAVE.get()] = getSquareSemiWave();
        res[WavColumn.WINDOWS_ENERGY.get()] = getWindowEnergy();
        res[WavColumn.TIME_FROM_CHANGE_POINT.get()] = getTimeFromChangeDirPoint();
        res[WavColumn.TIME_FROM_INFLECTION_POINT.get()] = getTimeFromInflectionPoint();
        res[WavColumn.MAGNITUDES_ANGLE.get()] = getMagnitudesAngle();
        res[WavColumn.WINDOW_CHANGE_POINTS_CNT.get()] = getWindowChangeDirPointsCnt();
        res[WavColumn.WINDOW_INFLECTION_POINTS_CNT.get()] = getWindowInflectionPointsCnt();
        res[WavColumn.FIRST_DERIVATIVE.get()] = getFirstDerivative();
        res[WavColumn.SECOND_DERIVATIVE.get()] = getSecondDerivative();
        return res;
    }
    
    public int columnsCnt(boolean sourceOnly) {
        return (sourceOnly) ? 2 : WavColumn.SIZE.get();
    }
    
    public void normalize() {
        setMagnitude((maxMagnitude == Double.MIN_VALUE) ? Double.MIN_VALUE : getMagnitude() / maxMagnitude);
        setWindowEnergy((maxWindowEnergy == Double.MIN_VALUE) ? Double.MIN_VALUE : getWindowEnergy() / maxWindowEnergy);
        setHarmonicIndex((maxHarmonicIndex == Double.MIN_VALUE) ? Double.MIN_VALUE : getHarmonicIndex() / maxHarmonicIndex);
        setSquareSemiWave((maxSquareSemiWave == Double.MIN_VALUE) ? Double.MIN_VALUE : getSquareSemiWave() / maxSquareSemiWave);
        setFirstDerivative((maxFirstDerivative == Double.MIN_VALUE) ? Double.MIN_VALUE : getFirstDerivative() / maxFirstDerivative);
        setSecondDerivative((maxSecondDerivative == Double.MIN_VALUE) ? Double.MIN_VALUE : getSecondDerivative() / maxSecondDerivative);
        setWindowInflectionPointsCnt((maxWindowInflectionPointsCnt == Double.MIN_VALUE) ? Double.MIN_VALUE : getWindowInflectionPointsCnt() / maxWindowInflectionPointsCnt);
        setWindowChangeDirPointsCnt((maxWindowChangeDirPointsCnt == Double.MIN_VALUE) ? Double.MIN_VALUE : getWindowChangeDirPointsCnt() / maxWindowChangeDirPointsCnt);
        setTimeFromChangeDirPoint((maxTimeFromChangeDirPoint == Double.MIN_VALUE) ? Double.MIN_VALUE : getTimeFromChangeDirPoint() / maxTimeFromChangeDirPoint);
        setTimeFromInflectionPoint((maxTimeFromInflectionPoint == Double.MIN_VALUE) ? Double.MIN_VALUE : getTimeFromInflectionPoint() / maxTimeFromInflectionPoint);
        //set.AAA((max.AAA == Double.MIN_VALUE) ? Double.MIN_VALUE : get.AAA() / max.AAA);
    }
    
    @Override
    public String toString() {
        return "HeartSoundPortion{" + "ts=" + ts + ", in=" + in + ", out=" + out + ", spectrum=" + spectrum + '}';
    }
}
