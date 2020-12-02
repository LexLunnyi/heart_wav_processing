package org.ll.heart.sound.recognition.fdomain;

import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author aberdnikov
 */
public class HHTPortion {
    final double[] data;
    final Complex[][] IMFs;

    public HHTPortion(double[] data) {
        this.data = data;
        IMFs = new Complex[10][data.length];
    }
    
    
    public void setIMF(int index, Complex[] IMF) {
        IMFs[index] = IMF;
    }
    

    public void save(String out) throws IOException {
        try (FileWriter fileWriter = new FileWriter(out + ".csv")) {
            for(int i = 0; i < data.length; i++) {
                String row = Double.toString(data[i]) + ";" +  
                             Double.toString(IMFs[0][i].getReal()) + ";" + 
                             Double.toString(IMFs[0][i].getArgument()) + "\n";
                fileWriter.write(row);
            }
        }
    }
}
