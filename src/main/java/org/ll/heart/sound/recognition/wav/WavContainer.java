package org.ll.heart.sound.recognition.wav;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.ll.heart.sound.recognition.HeartSoundPortion;
import org.ll.heart.sound.recognition.MagnitudeHistogram;
import org.ll.heart.sound.recognition.Options;
import org.ll.heart.sound.recognition.SxNode;

/**
 *
 * @author aberdnikov
 */
public class WavContainer {

    private final double HIST_THRES_MAGNITUDE = 0.75D;

    private final List<HeartSoundPortion> data = new ArrayList<>();
    private final List<SxNode> nodes = new ArrayList<>();
    private final List<String> spectrogram = new ArrayList<>();

    private final String fileName;
    private final SimpleDateFormat tsFormat = new SimpleDateFormat("mm:ss.S");

    private final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    private final MagnitudeHistogram mHisto;
    private final Options options;
    private final Date WAV_LIMIT_BEGIN;
    private final Date WAV_LIMIT_END;
    private final WavFile wavFile;
    private final int WINDOW_SIZE;
    private final int WINDOW_STEP;

    private Double maxWindowEnergy = Double.MIN_VALUE;
    private Double maxMagnitude = Double.MIN_VALUE;

    private Double maxValue = Double.MIN_VALUE;
    private Double minValue = Double.MAX_VALUE;
    private Double freqStep = 0.0D;

    public WavContainer(String fileName, Options options) throws IOException, Exception {
        this.fileName = fileName;
        this.options = options;
        this.WAV_LIMIT_BEGIN = new Date(options.getWavLengthMin());
        this.WAV_LIMIT_END = new Date(options.getWavLengthMax());
        this.WINDOW_SIZE = options.getWindowSize();
        this.WINDOW_STEP = options.getWindowStep();
        this.mHisto = new MagnitudeHistogram(WINDOW_SIZE, HIST_THRES_MAGNITUDE);
        long index = 0;
        // Open the wav file specified as the first argument
        wavFile = WavFile.openWavFile(new File(fileName));
        // Display information about the wav file
        wavFile.display();
        // Get the number of audio channels in the wav file
        int numChannels = wavFile.getNumChannels();
        if (numChannels > 1) {
            throw new Exception("Stereo records still not supported");
        }
        freqStep = (double) wavFile.getSampleRate() / (double) WINDOW_SIZE;
        // Create a buffer of N frames
        double[] buffer = new double[WINDOW_SIZE * numChannels];
        int framesRead;
        do {
            // Read frames into buffer
            framesRead = wavFile.readFrames(buffer, WINDOW_SIZE);
            // Loop through frames and look for minimum and maximum value
            for (int s = 0; s < framesRead * numChannels; s++) {
                index++;
                if (buffer[s] > maxValue) {
                    maxValue = buffer[s];
                }
                if (buffer[s] < minValue) {
                    minValue = buffer[s];
                }
                Date ts = new Date((index * 1000) / wavFile.getSampleRate());
                if (options.isWavLengthLimited()) {
                    if (ts.compareTo(WAV_LIMIT_BEGIN) < 0) {
                        continue;
                    }
                    if (ts.compareTo(WAV_LIMIT_END) > 0) {
                        framesRead = 0;
                        break;
                    }
                }
                data.add(new HeartSoundPortion(ts, buffer[s]));
            }
        } while (framesRead != 0);
        // Close the wavFile
        wavFile.close();
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void saveCSV(String fileName) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            long index = 0;
            for (HeartSoundPortion heartSoundPortion : data) {
                String Svals = (heartSoundPortion.isSx()) ? "1;" : "0;";
                Svals += (heartSoundPortion.isS1()) ? "1;" : "0;";
                Svals += (heartSoundPortion.isS2()) ? "1;" : "0;";
                //String row = tsFormat.format(heartSoundPortion.getTs()) + String.format(";%.5f;%.5f;%.5f;%.5f;\n", 
                //       heartSoundPortion.getIn(), heartSoundPortion.getOut(), heartSoundPortion.getMagnitude(), heartSoundPortion.getWindowEnergy());
                //String row = tsFormat.format(heartSoundPortion.getTs()) + String.format(";%.5f;%.5f;%s\n", 
                //                             heartSoundPortion.getIn(), heartSoundPortion.getMagnitude(), Svals);
                String row = String.format("%.5f;%.5f;%.5f;%s\n", (double) index / (double) wavFile.getSampleRate(),
                        heartSoundPortion.getIn(), heartSoundPortion.getMagnitude(), Svals);
                fileWriter.write(row);
                index++;
            }
        }
    }

    public void saveSpectrogramCSV() throws IOException {
        try (FileWriter fileWriter = new FileWriter("spectrogram.csv")) {
            for (String row : spectrogram) {
                fileWriter.write(row);
            }
        }
    }

    private Complex[] FourierProcessing(HeartSoundPortion curPortion, double[] input, Complex[] prev) {
        Complex[] res = transformer.transform(input, TransformType.FORWARD);
        int size = res.length;
        double curMagnitudeDiff = 0.0D;
        double curPhase = 0.0D;
        double curPhaseDiff = 0.0D;
        //String row = tsFormat.format(curPortion.getTs()) + ";";
        for (int i = 1; i < size / 2; i++) {
            double curFreq = i * freqStep;
            //Make bandpass filtration
            if ((curFreq <= options.getBandpassLow()) || (curFreq >= options.getBandpassHight())) {
                //Empty noise
                res[i] = new Complex(0);
                res[size - i] = new Complex(0);
            } else if (null != prev) {
                double diffAngle = calcPhaseSubtraction(prev[i], res[i], curFreq);
                double diffMagnitude = getMagnitudeSubtraction(prev[i], res[i], diffAngle);
                curPhaseDiff += diffAngle;
                curMagnitudeDiff += diffMagnitude;

                if (0.0D == curPhase) {
                    curPhase = calcPhase(res[i]);
                }
            }
            //row += String.format("%.5f;", res[i].abs());
        }
        //row += "\n";
        //spectrogram.add(row);
        curPortion.setMagnitude(curMagnitudeDiff);
        curPortion.setPhase(curPhase);
        curPortion.setPhaseDiff(curPhaseDiff);

        return res;
    }

    public void makeOutput() {
        long size = data.size();
        double[] input = new double[WINDOW_SIZE];
        int FIRST = WINDOW_SIZE / 2 - WINDOW_STEP / 2;

        Complex[] output = null;
        Complex[] fftData = null;
        double phase = 0.0D;
        double windowEnergy = 0.0D;
        double magnitude = 0.0D;
        double diffPhase = 0.0D;

        for (int index = 0; index < size; index += WINDOW_STEP) {
            System.out.print("index: " + Integer.toString(index) + "\n");
            if (index + WINDOW_SIZE >= size) {
                break;
            }
            for (int j = 0; j < WINDOW_SIZE; j++) {
                input[j] = data.get(index + j).getIn();
            }

            HeartSoundPortion curPortion = data.get(index + FIRST);
            fftData = FourierProcessing(curPortion, input, fftData);

            magnitude = curPortion.getMagnitude();
            phase = curPortion.getPhase();
            diffPhase = curPortion.getPhaseDiff();
            windowEnergy = 0;

            output = transformer.transform(fftData, TransformType.INVERSE);
            for (int j = FIRST; j < (FIRST + WINDOW_STEP); j++) {
                HeartSoundPortion cur = data.get(index + j);
                if (output[j].getReal() < 0) {
                    cur.setOut(output[j].abs() * (-1.0));
                } else {
                    cur.setOut(output[j].abs());
                }
                windowEnergy += Math.pow(cur.getOut(), 2);
            }

            //Set subband parameters
            for (int j = FIRST; j < (FIRST + WINDOW_STEP); j++) {
                HeartSoundPortion cur = data.get(index + j);
                cur.setWindowEnergy(windowEnergy);
                cur.setMagnitude(magnitude);
                cur.setPhase(phase);
                cur.setPhaseDiff(diffPhase);
            }
            updateExtremums(curPortion);
        }
        normalize();
        s1s2Detection();
        System.out.println(mHisto.toString());
        System.out.println("Threshold: " + mHisto.getThreshold() + "\n");
    }

    private double getMagnitudeSubtraction(Complex prev, Complex cur, double angle) {
        double fEdge = prev.abs();
        double sEdge = cur.abs();
        return Math.sqrt(Math.pow(fEdge, 2) + Math.pow(sEdge, 2) - 2 * fEdge * sEdge * Math.cos(angle * Math.PI));
    }

    private double calcPhase(Complex cur) {
        double arg = (Math.atan2(cur.getImaginary(), cur.getReal()) / Math.PI) * 180.0;
        if (cur.getImaginary() < 0) {
            arg += 360.0;
        }
        return arg / 360.0;
    }

    private double calcPhaseSubtraction(Complex prev, Complex cur, double curFreq) {
        double phaseOffset = ((double) WINDOW_STEP / (double) wavFile.getSampleRate()) * curFreq;
        double res = calcPhase(cur) - calcPhase(prev) + phaseOffset;
        if (res < -0.5) {
            res += 1.0;
        } else if (res > 0.5) {
            res -= 1.0;
        }
        return res;
    }

    private void updateExtremums(HeartSoundPortion curPortions) {
        if (curPortions.getMagnitude() > maxMagnitude) {
            maxMagnitude = curPortions.getMagnitude();
        }
        if (curPortions.getWindowEnergy() > maxWindowEnergy) {
            maxWindowEnergy = curPortions.getWindowEnergy();
        }
    }

    public void normalize() {
        for (HeartSoundPortion heartSoundPortion : data) {
            heartSoundPortion.setMagnitude(heartSoundPortion.getMagnitude() / maxMagnitude);
            heartSoundPortion.setWindowEnergy(heartSoundPortion.getWindowEnergy() / maxWindowEnergy);
            mHisto.push(heartSoundPortion.getMagnitude());
        }
    }

    public void s1s2Detection() {
        final Double THRESHOLD_MAGNITUDE = 0.2;
        final Double THRESHOLD_DURATION = 0.03;

        //Детектируем основные тона
        sxDetection(false);
        //Фильтруем основные тона по длительности и энергии
        int index = 0;
        while (nodes.size() > index) {
            SxNode sxNode = nodes.get(index); //Получаем элемент из массива
            //Если разница магнитуд зменьше порога - помечаем элемент на удаление
            boolean remove = (sxNode.getMaxMagnitude() < THRESHOLD_MAGNITUDE);
            //Если длительность тона ниже порога - помечаем элемент на удаления
            remove |= (((double) sxNode.getDuration() / (double) wavFile.getSampleRate()) < THRESHOLD_DURATION);
            if (remove) { //Обнуляем маркер Sx для ложных срабатываний
                for (long j = sxNode.getIndexBegin(); j < sxNode.getIndexSx(); j++) {
                    HeartSoundPortion sp = data.get((int) j);
                    sp.setSx(false);
                }
                nodes.remove(index);//Удаляем отфильтрованный элемент
            } else {
                index++;
            }
        }
        sxDetection(true);
        System.out.println("Nodes size: " + nodes.size() + "\n");
        //Собираем гистограммы по длительности и энергии
        MagnitudeHistogram magHist = new MagnitudeHistogram(16, 0.5D);
        MagnitudeHistogram durHist = new MagnitudeHistogram(16, 0.5D);
        for (SxNode sxNode : nodes) {
            magHist.push(sxNode.getMaxMagnitude());
            durHist.push(sxNode.getRelativeDuration());
        }
        System.out.println(magHist.toString("MAG"));
        System.out.println("MAG threshold: " + magHist.getThreshold());
        System.out.println(durHist.toString("DUR"));
        System.out.println("DUR threshold: " + durHist.getThreshold());

        //Классифицируем тона по порогам из гистограммы
        for (SxNode sxNode : nodes) {
            boolean S1 = false;
            boolean S2 = false;
            //Классифицируем тона по разнице магнитуд
            if (sxNode.getMaxMagnitude() > magHist.getThreshold()) {
                S1 = true;
            } else {
                S2 = true;
            }
            //Классифицируем тона по относительной длительности
            S1 &= (sxNode.getRelativeDuration() > durHist.getThreshold());
            S2 &= (sxNode.getRelativeDuration() <= durHist.getThreshold());
            for (long j = sxNode.getIndexBegin(); j < sxNode.getIndexSx(); j++) {
                //Маркируем отчеты исходного сигнала на принадлежность к S1 или S2
                HeartSoundPortion sp = data.get((int) j);
                sp.setS1(S1);
                sp.setS2(S2);
            }
        }
    }

    //Функция получения массивы осных тонов из массива выходных данных
    public void sxDetection(boolean useOwnSx) {
        //Очищаем массив основных тонов
        nodes.clear();
        SxNode sxNode = null;
        long index = 0;
        boolean predSx = false;
        //Определяем пороговое значение разницы магнитуд спектра
        double threshold = mHisto.getThreshold();
        for (HeartSoundPortion heartSoundPortion : data) {
            double magnitude = heartSoundPortion.getMagnitude();
            boolean Sx = (useOwnSx) ? heartSoundPortion.isSx() : (magnitude > threshold);
            //Считаем отчет принадлежащим основному тону (Sx=true) или нет (Sx=false)
            heartSoundPortion.setSx(Sx);
            if (Sx && !predSx) {//Если текущий отчет стал основным
                //Если есть предыдущий тон, то сохраняем его
                if (sxNode != null) {
                    sxNode.finalize(index);
                    nodes.add(sxNode);
                }
                //Создаем новую ноду
                sxNode = new SxNode(index);
            } else if (Sx && predSx) {//Если текущий отчет по-прежнему основной
                //Ищем максимальное значение магнитуды в тоне
                sxNode.processMagnitude(magnitude);
            } else if (!Sx && predSx) {//Если текущий отчет перестал быть основным
                //Кончился основной тон
                sxNode.SxDone(index);
            } else {//Если текущий отчет по-прежнему не оснвной
                //Идет систола или диастола
            }

            predSx = Sx;
            index++;
        }
    }

    public WavFile getWavFile() {
        return wavFile;
    }

    public int getWindowSize() {
        return WINDOW_SIZE;
    }

    public int getWindowStep() {
        return WINDOW_STEP;
    }
}
