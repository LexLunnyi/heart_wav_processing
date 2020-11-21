package org.ll.heart.sound.recognition.utils;

import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author aberdnikov
 */
public class FIFOStatisticTest {

    /**
     * Test of the subtraction when the container is empty
     */
    @Test
    public void testEmpty() {
        FIFOStatistic instance = new FIFOStatistic();
        Throwable thrown = assertThrows(NoSuchElementException.class, () -> {
            instance.subtract(0.1);
        });
        assertNotNull(thrown.getMessage());
    }

    /**
     * Test of parameters with only one element
     */
    @Test
    public void testOneElem() {
        double value = 1.0;
        FIFOStatistic instance = new FIFOStatistic();
        instance.add(value);
        
        assertEquals(value, instance.getMean());
        assertEquals(value, instance.getMin());
        assertEquals(value, instance.getMax());
        assertEquals(0.0, instance.getVariance());
        assertEquals(0.0, instance.getStandardDeviation());
    }
    
    /**
     * Test of parameters with many elements
     */
    @Test
    public void testManyElem() {
        FIFOStatistic instance = new FIFOStatistic();
        instance.add(3.0); // It is 1 2 3 4 5
        instance.add(1.0);
        instance.add(2.0);
        instance.add(5.0);
        instance.add(4.0);
        
        assertEquals(3.0, instance.getMean());
        assertEquals(1.0, instance.getMin());
        assertEquals(5.0, instance.getMax());
        assertEquals(2.0, instance.getVariance());
        assertEquals(Math.sqrt(2.0), instance.getStandardDeviation());
    }
    
    /**
     * Test of parameters with many elements after removing some of them
     */
    @Test
    public void testManyElemAfterSubtract() {
        FIFOStatistic instance = new FIFOStatistic();
        instance.add(0.5); // It is 6 7 8 9 10
        instance.add(12.0);
        instance.add(6.0);
        instance.add(7.0);
        instance.add(8.0);
        instance.add(9.0);
        instance.add(10.0);
        instance.subtract(0.5);
        instance.subtract(12.0);
        
        assertEquals(8.0, instance.getMean());
        assertEquals(6.0, instance.getMin());
        assertEquals(10.0, instance.getMax());
        assertEquals(2.0, instance.getVariance());
        assertEquals(Math.sqrt(2.0), instance.getStandardDeviation());
    }
}
