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
    final int IMFCount;
    int maxSet = 0;
    Complex cur = new Complex(0, 0);
    double MFreq = 0.0;

    public HHTPortion(double[] data, int count) {
        this.IMFCount = count;
        this.data = data;
        IMFs = new Complex[IMFCount][data.length];
    }

    public void setIMF(int index, Complex[] IMF) {
        IMFs[index] = IMF;
        maxSet = Math.max(maxSet, index);
    }
    
    public void calcFeatures() {
        for (int j = 0; j < maxSet; j++) {
            double dfi = IMFs[j][1].getArgument() - IMFs[j][0].getArgument();
            MFreq += (j+1)*(Math.PI / dfi)*IMFs[j][0].abs();
            cur.add(IMFs[j][0]);
        }
    }
    
    public double getMagnitude() {
        return cur.abs();
    }
    
    public double getMFreq() {
        return MFreq;
    }

    public void save(String out) throws IOException {
//        try (FileWriter fileWriter = new FileWriter(out + ".csv")) {
//            for (int i = 0; i < data.length; i++) {
//
//                StringBuilder sb = new StringBuilder();
//                sb.append(Double.toString(data[i])).append(";");
//                for (int j = 0; j < maxSet; j++) {
//                    sb.append(Double.toString(IMFs[j][i].getReal())).append(";");
//                }
//                sb.append("\n");
//                fileWriter.write(sb.toString());
//            }
//        }
    }
}
