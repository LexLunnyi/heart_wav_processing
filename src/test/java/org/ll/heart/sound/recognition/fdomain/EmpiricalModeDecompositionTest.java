package org.ll.heart.sound.recognition.fdomain;

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
        Field resMinX = EmpiricalModeDecomposition.class.getDeclaredField("minX");
        resMinX.setAccessible(true);
        Field resMinY = EmpiricalModeDecomposition.class.getDeclaredField("minY");
        resMinX.setAccessible(true);
        
        List<Double> minX = new ArrayList<>();
        minX.add(7.0);
        List<Double> minY = new ArrayList<>();
        minY.add(-0.3);
        assertTrue(resMinX.get(emd).equals(minX));
        assertTrue(resMinY.get(emd).equals(minY));
    }
    
     @Test
    public void testMaxExtremums() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        EmpiricalModeDecomposition emd = new EmpiricalModeDecomposition(data, 10);
        Field resMaxX = EmpiricalModeDecomposition.class.getDeclaredField("maxX");
        resMaxX.setAccessible(true);
        Field resMaxY = EmpiricalModeDecomposition.class.getDeclaredField("maxY");
        resMaxX.setAccessible(true);
        
        List<Double> maxX = new ArrayList<>();
        maxX.add(2.0);
        List<Double> maxY = new ArrayList<>();
        maxY.add(0.3);
        assertTrue(resMaxX.get(emd).equals(maxX));
        assertTrue(resMaxY.get(emd).equals(maxY));
    }
    
    
    

    /**
     * Test of hasIMF method, of class EmpiricalModeDecomposition.
     */
    @Test
    public void testHasIMF() {
        EmpiricalModeDecomposition emd = new EmpiricalModeDecomposition(data, 10);
        //There are only two knots (1 min and 1 max) but we need at least 6.
        assertFalse(emd.hasIMF());
    }

    /**
     * Test of getIMF method, of class EmpiricalModeDecomposition.
     */
    @Test
    public void testGetIMF() {
    }
    
}
