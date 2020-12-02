package org.ll.heart.sound.recognition.fdomain;

import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author aberdnikov
 */
public class EmpiricalModeDecompositionTest {
    private final double[] data = new double[] {0.1, 0.2, 0.3, 0.2, 0.1, -0.1, -0.2, -0.3, -0.2, -0.1};
    
    public EmpiricalModeDecompositionTest() {
    }

    @Test
    public void testTrend() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        EmpiricalModeDecomposition emd = new EmpiricalModeDecomposition(data, 10);
        Method method = EmpiricalModeDecomposition.class.getDeclaredMethod("trendIsUp", double[].class);
        method.setAccessible(true);
        assertTrue((boolean)method.invoke(emd, data));
    }
    
    @Test
    public void testMinExtremums() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        EmpiricalModeDecomposition emd = new EmpiricalModeDecomposition(data, 10);
        Field resMin = EmpiricalModeDecomposition.class.getDeclaredField("min");
        resMin.setAccessible(true);
        
        List<Point2D.Double> min = new ArrayList<>();
        min.add(new Point2D.Double(0, data[0]));
        min.add(new Point2D.Double(7, -0.3));
        min.add(new Point2D.Double(data.length-1, data[data.length-1]));
        
        assertTrue(resMin.get(emd).equals(min));
    }
    
     @Test
    public void testMaxExtremums() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        EmpiricalModeDecomposition emd = new EmpiricalModeDecomposition(data, 10);
        Field resMax = EmpiricalModeDecomposition.class.getDeclaredField("max");
        resMax.setAccessible(true);
        
        List<Point2D.Double> max = new ArrayList<>();
        max.add(new Point2D.Double(0, data[0]));
        max.add(new Point2D.Double(2, 0.3));
        max.add(new Point2D.Double(data.length-1, data[data.length-1]));
        
        assertTrue(resMax.get(emd).equals(max));
    }

    /**
     * Test of hasIMF method, of class EmpiricalModeDecomposition.
     */
    @Test
    public void testHasIMF() {
        EmpiricalModeDecomposition emd = new EmpiricalModeDecomposition(data, 10);
        assertTrue(emd.hasIMF());
    }

    /**
     * Test of getIMF method, of class EmpiricalModeDecomposition.
     */
    @Test
    public void testGetIMF() {
    }
    
}
