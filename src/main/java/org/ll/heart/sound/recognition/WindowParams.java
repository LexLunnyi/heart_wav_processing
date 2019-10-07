package org.ll.heart.sound.recognition;

/**
 *
 * @author aberdnikov
 */
public class WindowParams {
    final int size;
    final int step;

    public WindowParams(int frequencyRate, int sampleRate) {
        int curSize = sampleRate / frequencyRate;
        int min = sampleRate;
        int sizeIndex = 1;
        for(int i = 1; i <= 10; i++) {
            int base = (int) Math.pow(2, i);
            int diff = Math.abs(curSize - base);
            if (diff < min) {
                min = diff;
                sizeIndex = i;
            }
        }
        int stepIndex = sizeIndex - 5;
        if (stepIndex < 1) {
            stepIndex = 1;
        }
        size = (int)Math.pow(2, sizeIndex);
        step = (int)Math.pow(2, stepIndex);
        System.out.println("WINDOW AUTO: size -> " + Integer.toString(size));
        System.out.println("WINDOW AUTO: step -> " + Integer.toString(step));
    }

    public int getSize() {
        return size;
    }

    public int getStep() {
        return step;
    }
}
