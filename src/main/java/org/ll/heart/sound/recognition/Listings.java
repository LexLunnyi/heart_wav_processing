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
    private static final int WINDOW_STEP = 32;
    private long sampleRate = 11025;
    
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
            HeartSoundPortion curPortion = data.get(index + FIRST);
            fftData = calcMagnitude(curPortion, input, fftData);
            double magnitude = curPortion.getMagnitude();
            //Выполняем обратное преобразование Фурье для получения отфильтрованного сигнала
            output = transformer.transform(fftData, TransformType.INVERSE);
            for(int j = FIRST; j < (FIRST + WINDOW_STEP); j++) {
                //Сохраняем результаты в выходной массив
                HeartSoundPortion cur = data.get(index + j);
                cur.setMagnitude(magnitude);
                if (output[j].getReal() < 0) {
                    cur.setOut(output[j].abs()*(-1.0));
                } else {
                    cur.setOut(output[j].abs());
                }
            }
        }
    }
    
    
    //Функция для полосовой фильтрации и получения разницы магнитуд спектров
    private Complex[] calcMagnitude(HeartSoundPortion curPortion, double[] input, Complex[] prev) {
        //Задаем границы фильтра при помощи констант
        final double SPECTRUM_LOW = 55.0;
        final double SPECTRUM_HIGH = 165.0;
        //Выполнеям прямое преобразование Фурье для получения спектра сигнала
        Complex[] res = transformer.transform(input, TransformType.FORWARD);
        
        int size = res.length;
        double curMagnitudeDiff = 0.0D;
        for (int i = 1; i < size/2; i++) {
            //Определяем частоту текущей гармоники
            double curFreq = i * freqStep;
            
            //Устанавливаем амплитуду гармоники в 0, если она за пределами диапазона
            if ((curFreq <= SPECTRUM_LOW) || (curFreq >= SPECTRUM_HIGH)) {
                res[i] = new Complex(0);
                res[size-i] = new Complex(0);
            } else if (null != prev) {
                //Получаем разницу фаз между моментами расчета спектра для i-ой гармоники
                double diffAngle = calcPhaseSubtraction(prev[i], res[i], curFreq);
                //Получаем разницу магнитуд для i-той гармоники
                curMagnitudeDiff += getMagnitudeSubtraction(prev[i], res[i], diffAngle);
            }
        }
        curPortion.setMagnitude(curMagnitudeDiff);
        return res;
    }
    
    
    //Функция для получения фазы гармоники (в радианах)
    private double calcPhase(Complex cur) {
        double arg = Math.atan2(cur.getImaginary(), cur.getReal());
        if (cur.getImaginary() < 0) {
            arg += 2.0*Math.PI;
        }
        return arg;
    }
    
    
    //Функция для вычисления разницы фаз одной гармоники в два соседних момента времени
    private double calcPhaseSubtraction(Complex prev, Complex cur, double curFreq) {
        //Получаем смещенние фаз гармоники, которое должно быть между
        final double phaseOffset = ((double)WINDOW_STEP / (double)sampleRate)*curFreq;
        //Приводим угол к расположению в I,IV четвертях для дальнейших вычислений
        double res = calcPhase(cur) - calcPhase(prev) + phaseOffset;
        if (res < -1.0 * Math.PI) {
            res += 2.0*Math.PI;
        } else if (res > Math.PI) {
            res -= 2.0*Math.PI;
        }
        return res;
    }
    
    
    //Функция расчета разницы магнитуд для одной гармоники
    private double getMagnitudeSubtraction(Complex prev, Complex cur, double angle) {
        //Магнитуда гармоники в момент времени t(i-1)
        double prevMagnitude = prev.abs();
        //Магнитуда гармоники в момент времени t(i)
        double curMagnitude = cur.abs();
        return Math.sqrt(Math.pow(prevMagnitude, 2) + Math.pow(curMagnitude, 2) - 2*prevMagnitude*curMagnitude*Math.cos(angle));
    }
}
