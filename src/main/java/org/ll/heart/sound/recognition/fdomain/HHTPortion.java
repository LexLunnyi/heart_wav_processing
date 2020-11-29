package org.ll.heart.sound.recognition.fdomain;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author aberdnikov
 */
public class HHTPortion {
    final double[] data;
    final double[] top;
    final double[] bottom;

    public HHTPortion(double[] data, double[] top, double[] bottom) {
        this.data = data;
        this.top = top;
        this.bottom = bottom;
    }

    public void save(String out) throws IOException {
        try (FileWriter fileWriter = new FileWriter(out + ".csv")) {
            for(int i = 0; i < data.length; i++) {
                String row = Double.toString(data[i]) + ";" +  
                             Double.toString(top[i]) + ";" + 
                             Double.toString(bottom[i]) + "\n";
                fileWriter.write(row);
            }
        }
    }
}
