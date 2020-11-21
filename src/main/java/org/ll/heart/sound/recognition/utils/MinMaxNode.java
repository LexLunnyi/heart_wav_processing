package org.ll.heart.sound.recognition.utils;

/**
 *
 * @author aberdnikov
 */
public class MinMaxNode {
    final double min;
    final double max;
    final double value;

    public MinMaxNode(double value) {
        this.value = value;
        this.min = value;
        this.max = value;
    }

    public MinMaxNode(double value, MinMaxNode prev) {
        this.value = value;
        min = Math.min(value, prev.getMin());
        max = Math.max(value, prev.getMax());
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getValue() {
        return value;
    }
}
