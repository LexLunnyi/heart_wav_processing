1. Добавить к точкам для интерполяции начало и конец диапазона.
2. Сохранить проверить что огибающая строится.
3. Преобразование Гильберта.





package org.ll.heart.sound.recognition.fdomain;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class EmpiricalModeDecomposition {
    final double[] src;
    final int size;
    int IMFindex = 0;
    final int IMFindexThreshold;
    double[] sifted = null;
    
    //knots for cubic splines
    List<Double> minX = new ArrayList<>();
    List<Double> minY = new ArrayList<>();
    List<Double> maxX = new ArrayList<>();
    List<Double> maxY = new ArrayList<>();
    
    public EmpiricalModeDecomposition(double[] src, int threshold) {
        this.src = src;
        this.size = src.length;
        IMFindexThreshold = threshold;
        extremums(src);
    }
    
    public boolean hasIMF() {
        if (IMFindex >= IMFindexThreshold) {
            return false;
        }
        //Min size for knots list is 3
        return ((minX.size() >= 3) && (maxX.size() >= 3));
    }
    
    public double[] getIMF(SignalPortion portion) {
        double[] data = (sifted == null) ? src : sifted;
               
        PolynomialSplineFunction bottomSpline = spline(minX, minY);
        PolynomialSplineFunction topSpline = spline(maxX, maxY);
        
        double[] mean = new double[size];
        double[] IMF = new double[size];
        //FIXME: only for debug
        double[] top = new double[size];
        double[] bottom = new double[size];
        
        for(int i = 0; i < size; i++) {
            top[i] = topSpline.value(i);
            bottom[i] = bottomSpline.value(i);
            mean[i] = (top[i] + bottom[i]) / 2.0;
            IMF[i] = data[i] - mean[i];
        }
        sifted = mean;
        
        //debug
        if (0 == IMFindex) {
            portion.setFreqAdd(new HHTPortion(data, top, bottom));
        }
        
        IMFindex++;
        
        extremums(src);
        return IMF;
    }
    
    
    
    
    
    private boolean trendIsUp(double[] data) throws IllegalStateException {
        int len = data.length;
        double prev = data[0];
        for (int i = 1; i < len; i++) {
            double cur = data[i];
            if (cur > prev) {
                return true;
            } else if (cur < prev) {
                return false;
            }
            prev = cur;
        }
        throw new IllegalStateException("There is no trend in the window");
    }    
    
    
    private void extremums(double[] data) throws IllegalStateException {
        minX.clear();
        minY.clear();
        maxX.clear();
        maxY.clear();
        
        double prev = data[0];
        boolean lastMin = trendIsUp(data);
        
        for (int i = 1; i < size; i++) {
            double cur = data[i];
            
            if (cur > prev) {
                //If before was local max then prev is local min
                if (!lastMin) {
                    //Add new min
                    minX.add((double)(i-1));
                    minY.add(prev);
                    lastMin = true;
                }
            } else if (cur < prev) {
                //If before was local min then prev is local max
                if (lastMin) {
                    //Add new min
                    maxX.add((double)(i-1));
                    maxY.add(prev);
                    lastMin = false;
                }
            }
            
            prev = cur;
        }
    }
    
    
    private PolynomialSplineFunction spline(List<Double> x, List<Double> y) {
        //Convert mins and max to knots
        int len = x.size();
        double[] xIn = new double[len];
        double[] yIn = new double[len];
        for(int i = 0; i < len; i++) {
            xIn[i] = x.get(i);
            yIn[i] = y.get(i);
        }
        //Get spline function
        SplineInterpolator si = new SplineInterpolator();
        return si.interpolate(xIn, yIn);
    }
}
