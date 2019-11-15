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
    MAGNITUDE(2),
    SX(3),
    MAX_HARMONIC_INDEX(4),
    SQUARE_SEMI_WAVE(5),
    WINDOWS_ENERGY(6),
    TIME_FROM_CHANGE_POINT(7),
    TIME_FROM_INFLECTION_POINT(8),
    MAGNITUDES_ANGLE(9),
    WINDOW_CHANGE_POINTS_CNT(10),
    WINDOW_INFLECTION_POINTS_CNT(11),
    FIRST_DERIVATIVE(12),
    SECOND_DERIVATIVE(13),
    SIZE(14);
    
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
    private Double maxHarmonic;
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
    
    
    private final List<HeartSoundSpectrumPortion> spectrum = new ArrayList<>();

    public HeartSoundPortion(Date ts, Double in) {
        this.ts = ts;
        this.in = in;
        this.out = 0.0D;
        this.magnitude = 0.0D;
        this.phase = 0.0D;
        this.windowEnergy = 0.0D;
        this.maxHarmonic = 0.0D;
        this.squareSemiWave = 0.0D;
        this.timeFromChangeDirPoint = 0.0D;
        this.timeFromInflectionPoint = 0.0D;
        this.magnitudesAngle = 0.0D;
        this.firstDerivative = 0.0D;
        this.secondDerivative = 0.0D;
        this.windowChangeDirPointsCnt = 0.0D;
        this.windowInflectionPointsCnt = 0.0D;
        this.Sx = false;
        this.S1 = false;
        this.S2 = false;
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
    
    public Double getWindowEnergy() {
        return windowEnergy;
    }

    public void setWindowEnergy(Double windowEnergy) {
        this.windowEnergy = windowEnergy;
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

    public Double getMaxHarmonic() {
        return maxHarmonic;
    }

    public void setMaxHarmonic(Double maxHarmonic) {
        this.maxHarmonic = maxHarmonic;
    }

    public Double getSquareSemiWave() {
        return squareSemiWave;
    }

    public void setSquareSemiWave(Double squareSemiWave) {
        this.squareSemiWave = squareSemiWave;
    }

    public Double getTimeFromChangeDirPoint() {
        return timeFromChangeDirPoint;
    }

    public void setTimeFromChangeDirPoint(Double timeFromChangeDirPoint) {
        this.timeFromChangeDirPoint = timeFromChangeDirPoint;
    }

    public Double getTimeFromInflectionPoint() {
        return timeFromInflectionPoint;
    }

    public void setTimeFromInflectionPoint(Double timeFromInflectionPoint) {
        this.timeFromInflectionPoint = timeFromInflectionPoint;
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
    }

    public Double getSecondDerivative() {
        return secondDerivative;
    }

    public void setSecondDerivative(Double secondDerivative) {
        this.secondDerivative = secondDerivative;
    }

    public Double getWindowChangeDirPointsCnt() {
        return windowChangeDirPointsCnt;
    }

    public void setWindowChangeDirPointsCnt(Double windowChangeDirPointsCnt) {
        this.windowChangeDirPointsCnt = windowChangeDirPointsCnt;
    }

    public Double getWindowInflectionPointsCnt() {
        return windowInflectionPointsCnt;
    }

    public void setWindowInflectionPointsCnt(Double windowInflectionPointsCnt) {
        this.windowInflectionPointsCnt = windowInflectionPointsCnt;
    }
    
    
    
    public Double[] getColumns() {
        Double[] res = new Double[WavColumn.SIZE.get()];
        res[WavColumn.TIME.get()] = (double)getTs().getTime();
        res[WavColumn.SIGNAL.get()] = getIn();
        res[WavColumn.MAGNITUDE.get()] = getMagnitude();
        res[WavColumn.SX.get()] = (isSx()) ? 1.0D : 0.0D;
        res[WavColumn.MAX_HARMONIC_INDEX.get()] = getMaxHarmonic();
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
    
    public int columnsCnt() {
        return WavColumn.SIZE.get();
    }
    
    @Override
    public String toString() {
        return "HeartSoundPortion{" + "ts=" + ts + ", in=" + in + ", out=" + out + ", spectrum=" + spectrum + '}';
    }
}
