package org.ll.heart.sound.recognition.windowing;

/**
 * @author aberdnikov
 */
public class WProcessorRectangle extends WindowingProcessor {
    @Override
    double getW(double i) {
        return 1.0;
    }
}
