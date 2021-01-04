package org.ll.heart.sound.recognition.utils;

import java.awt.geom.Point2D;
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
    List<Point2D.Double> min = new ArrayList<>();
    List<Point2D.Double> max = new ArrayList<>();
    
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
        return ((min.size() >= 3) && (max.size() >= 3));
    }
    
    public double[] getIMF(SignalPortion portion) {
        double[] data = (sifted == null) ? src : sifted;
               
        PolynomialSplineFunction bottomSpline = spline(min);
        PolynomialSplineFunction topSpline = spline(max);
        
        double[] mean = new double[size];
        double[] IMF = new double[size];
        //FIXME: only for report.
        double[] top = new double[size];
        double[] bottom = new double[size];
        
        for(int i = 0; i < size; i++) {
            top[i] = topSpline.value(i);
            bottom[i] = bottomSpline.value(i);
            mean[i] = (top[i] + bottom[i]) / 2.0;
            IMF[i] = data[i] - mean[i];
        }
        sifted = mean;
        
        //FIXME it is currently needed for the figure of the envelops in the report
        //if (0 == IMFindex) {
        //    portion.setFreqAdd(new HHTPortion(data, top, bottom));
        //}
        
        IMFindex++;
        
        extremums(sifted);
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
        min.clear();
        max.clear();
        double prev = data[0];
        min.add(new Point2D.Double(0.0, prev));
        max.add(new Point2D.Double(0.0, prev));
        boolean lastMin = trendIsUp(data);
        for (int i = 1; i < size; i++) {
            double cur = data[i];
            
            if (cur > prev) {
                //If before was local max then prev is local min
                if (!lastMin) {
                    //Add new min
                    min.add(new Point2D.Double(i-1, prev));
                    lastMin = true;
                }
            } else if (cur < prev) {
                //If before was local min then prev is local max
                if (lastMin) {
                    //Add new min
                    max.add(new Point2D.Double(i-1, prev));
                    lastMin = false;
                }
            }
            
            prev = cur;
        }
        min.add(new Point2D.Double(size-1, data[size-1]));
        max.add(new Point2D.Double(size-1, data[size-1]));
    }
    
    
    private PolynomialSplineFunction spline(List<Point2D.Double> points) {
        //Convert mins and max to knots
        int len = points.size();
        double[] xIn = new double[len];
        double[] yIn = new double[len];
        for(int i = 0; i < len; i++) {
            xIn[i] = points.get(i).getX();
            yIn[i] = points.get(i).getY();
        }
        //Get spline function
        SplineInterpolator si = new SplineInterpolator();
        return si.interpolate(xIn, yIn);
    }
}
