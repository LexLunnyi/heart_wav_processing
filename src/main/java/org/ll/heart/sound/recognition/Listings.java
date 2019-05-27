/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ll.heart.sound.recognition;

import java.util.List;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 *
 * @author aberdnikov
 */
public class Listings {
    public void makeFourierTransform(List<HeartSoundPortion> data) {
        final int WINDOW_SIZE = 32;
        final int WINDOW_STEP = 8;
        //Инициализируем переменые
        long size = data.size();
        double[] input = new double[WINDOW_SIZE];
        Complex[] fftData = null;
        //В цикле проходим через весь файл с шагом WINDOW_STEP
        for(int index = 0; index < size; index += WINDOW_STEP) {
            if (index + WINDOW_SIZE >= size) {
                break;
            }
            //Заполняем данными входной массив
            for(int j = 0; j < WINDOW_SIZE; j++) {
                input[j] = data.get(index + j).getIn();
            }
            //Выпалняем Быстрое Преобразование Фурье
            FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
            fftData = transformer.transform(input, TransformType.FORWARD);
            
            /*
            Выполняем дальнейшую обработку fftData
            ...
            */
        }
    }
}
