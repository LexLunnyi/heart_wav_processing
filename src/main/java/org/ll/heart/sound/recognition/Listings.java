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
    private Double freqStep = 0.0D;
    FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    
    public void makeFourierTransform(List<HeartSoundPortion> data) {
        final int WINDOW_SIZE = 32;
        final int WINDOW_STEP = 8;
        int FIRST = WINDOW_SIZE/2 - WINDOW_STEP/2;
        //Инициализируем переменые
        long size = data.size();
        double[] input = new double[WINDOW_SIZE];
        Complex[] output = null;
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
            //Выпалняем Быстрое Преобразование Фурье с фильтрацией гармоник
            fftData = bandpass(input, fftData);
            //Выполняем обратное преобразование Фурье для получения отфильтрованного сигнала
            output = transformer.transform(fftData, TransformType.INVERSE);
            for(int j = FIRST; j < (FIRST + WINDOW_STEP); j++) {
                //Сохраняем отфильтрованные данные в выходной массив
                HeartSoundPortion cur = data.get(index + j);
                if (output[j].getReal() < 0) {
                    cur.setOut(output[j].abs()*(-1.0));
                } else {
                    cur.setOut(output[j].abs());
                }
            }
        }
    }
    
    
    
    private Complex[] bandpass(double[] input, Complex[] prev) {
        //Задаем границы фильтра при помощи констант
        final double SPECTRUM_LOW = 55.0;
        final double SPECTRUM_HIGH = 165.0;
        //Выполнеям прямое преобразование Фурье для получения спектра сигнала
        Complex[] res = transformer.transform(input, TransformType.FORWARD);
        
        int size = res.length;
        for (int i = 1; i < size/2; i++) {
            //Определяем частоту текущей гармоники
            double curFreq = i * freqStep;
            
            //Устанавливаем амплитуду гармоники в 0, если она за пределами диапазона
            if ((curFreq <= SPECTRUM_LOW) || (curFreq >= SPECTRUM_HIGH)) {
                res[i] = new Complex(0);
                res[size-i] = new Complex(0);
            } else if (null != prev) {
                /*
                Здесь наодятся "полезные" гармоники, выполняем над ними дальнейшие операции
                ...
                */
            }
        }
        return res;
    }
}
