package org.ll.heart.sound.recognition.utils;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author aberdnikov
 */
public class AdaptiveHistogramTest {
    /**
     * Test of add method, of class Histogram.
     */
    @Test
    public void testCommon() {
        List<Double> in = new LinkedList<>();
        in.add(0.4);
        in.add(0.9);
        in.add(1.4);
        in.add(1.9);
        in.add(2.4);
        in.add(2.9);
        in.add(3.4);
        in.add(3.9);
        in.add(4.4);
        in.add(5.0);
        AdaptiveHistogram instance = new AdaptiveHistogram(in, 10, 0.5);
        instance.add(4.9);
        instance.add(5.9);
        instance.add(6.4);
        instance.add(6.9);
        instance.add(7.4);
        instance.add(7.9);
        instance.add(8.4);
        instance.add(8.9);
        instance.add(9.4);
        instance.add(9.9);
        assertEquals(5.0, instance.getThreshold());
        instance.add(9.9);
        instance.add(9.8);
        assertEquals(5.5, instance.getThreshold());
    }
    
}
