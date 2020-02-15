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
import org.ll.heart.sound.recognition.WindowParams;


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
    
    private Double maxValue = Double.MIN_VALUE;
    private Double minValue = Double.MAX_VALUE;
    private Double freqStep = Double.MIN_VALUE;
    
    private final boolean sourceOnly;

    public WavContainer(String fileName, Options options, boolean sourceOnly) throws IOException, Exception {
        this.fileName = fileName;
        this.options = options;
        this.WAV_LIMIT_BEGIN = new Date(options.getWavLengthMin());
        this.WAV_LIMIT_END = new Date(options.getWavLengthMax());
        this.sourceOnly = sourceOnly;
        
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
        //Get window size and step
        if (options.isWindowAuto()) {
            WindowParams autoParams = new WindowParams(options.getWindowFrequencyRate(), (int)wavFile.getSampleRate());
            this.WINDOW_SIZE = autoParams.getSize();
            this.WINDOW_STEP = autoParams.getStep();
        } else {
            this.WINDOW_SIZE = options.getWindowSize();
            this.WINDOW_STEP = options.getWindowStep();
        }
        this.mHisto = new MagnitudeHistogram(WINDOW_SIZE, HIST_THRES_MAGNITUDE);
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

    public void saveCSV(String outputPath, String curName) throws IOException {
        String outFileName = outputPath + "/" + curName + ".csv";
        File file = new File(outputPath);
        file.mkdir();
        try (FileWriter fileWriter = new FileWriter(outFileName)) {
            fileWriter.write(data.get(0).getColumnsNames(sourceOnly) + "\n");
            for (HeartSoundPortion heartSoundPortion : data) {
                Double[] all = heartSoundPortion.getColumns(sourceOnly);
                String row = "";
                int cSize = heartSoundPortion.columnsCnt(sourceOnly);
                for(int j = 0; j < cSize; j++) {
                    row += Double.toString(all[j]);
                    if (j != cSize - 1) {
                        row += ";";
                    }
                }
                row += "\n";
                fileWriter.write(row);
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
        Complex tmp = new Complex(0, 0);
        double harmonics = Double.MIN_VALUE;
        double windowEnergy = Double.MIN_VALUE;
        
        //String row = tsFormat.format(curPortion.getTs()) + ";";
        for (int i = 1; i < size / 2; i++) {
            double curFreq = i * freqStep;
            //Make bandpass filtration
            if ((curFreq <= options.getBandpassLow()) || (curFreq >= options.getBandpassHight())) {
                //Empty noise
                res[i] = new Complex(0);
                res[size - i] = new Complex(0);
            } else if (null != prev) {
                harmonics += res[i].abs() * curFreq;
                Complex diff = prev[i].subtract(res[i]);
                tmp = tmp.add(diff);
                windowEnergy += res[i].abs();
            }
            //row += String.format("%.5f;", res[i].abs());
        }
        //row += "\n";
        //spectrogram.add(row);
        curPortion.setMagnitude(tmp.abs());
        curPortion.setPhase(0.0);//FIXME currently not needed
        curPortion.setMagnitudesAngle(tmp.getArgument());
        curPortion.setHarmonicIndex(harmonics);
        curPortion.setWindowEnergy(windowEnergy);

        return res;
    }

    public void makeOutput() {
        if (sourceOnly) {
            return;
        }
        long size = data.size();
        double[] input = new double[WINDOW_SIZE];
        int FIRST = WINDOW_SIZE / 2 - WINDOW_STEP / 2;

        Complex[] output = null;
        Complex[] fftData = null;
        double phase = Double.MIN_VALUE;
        double windowEnergy = Double.MIN_VALUE;
        double magnitude = Double.MIN_VALUE;
        double diffPhase = Double.MIN_VALUE;
        double harmonicIndex = Double.MIN_VALUE;
        HeartSoundPortion.init();
        
        for (int index = 0; index < size; index += WINDOW_STEP) {
            //System.out.print("index: " + Integer.toString(index) + "\n");
            if (index + WINDOW_SIZE >= size) {
                break;
            }
            for (int j = 0; j < WINDOW_SIZE; j++) {
                input[j] = data.get(index + j).getIn();
            }

            HeartSoundPortion curPortion = data.get(index);
            fftData = FourierProcessing(curPortion, input, fftData);

            magnitude = curPortion.getMagnitude();
            phase = curPortion.getPhase();
            diffPhase = curPortion.getMagnitudesAngle();
            windowEnergy = curPortion.getWindowEnergy();
            harmonicIndex = curPortion.getHarmonicIndex();
            
            output = transformer.transform(fftData, TransformType.INVERSE);
            
            
            for (int j = FIRST; j < (FIRST + WINDOW_STEP); j++) {
                HeartSoundPortion cur = data.get(index + j);
                //Save filtred data
                cur.setMagnitude(magnitude);
                if (output[j].getReal() < 0) {
                    cur.setOut(output[j].abs()*(-1.0));
                } else {
                    cur.setOut(output[j].abs());
                }
            }
/*
            //Set subband parameters
            for (int j = FIRST; j < (FIRST + WINDOW_STEP); j++) {
                HeartSoundPortion cur = data.get(index + j);
                cur.setWindowEnergy(windowEnergy);
                cur.setMagnitude(magnitude);
                cur.setPhase(phase);
                cur.setMagnitudesAngle(diffPhase);
                cur.setHarmonicIndex(harmonicIndex);
            }
*/
        }
        
        calcParameters();
 
        int changeDirectionPointCount = 0;
        int inflectionPointCount = 0;
        /*
        for (int index = 0; index < size - WINDOW_SIZE; index++ ) {
            changeDirectionPointCount = 0;
            inflectionPointCount = 0;
            for (int j = FIRST; j < (FIRST + WINDOW_SIZE); j++) {
                HeartSoundPortion cur = data.get(index + j);
                //Calc change direction and inflection points
                if (cur.isChangeDirectionPoint()) {
                    changeDirectionPointCount++;
                }
                if (cur.isInflectionPoint()) {
                    inflectionPointCount++;
                }
            }
            HeartSoundPortion cur = data.get(index);
            cur.setWindowChangeDirPointsCnt((double)changeDirectionPointCount);
            cur.setWindowInflectionPointsCnt((double)inflectionPointCount);
        }
        for (int index = (int)size - WINDOW_SIZE; index < size; index++) {
            HeartSoundPortion cur = data.get(index);
            cur.setWindowChangeDirPointsCnt((double)changeDirectionPointCount);
            cur.setWindowInflectionPointsCnt((double)inflectionPointCount);
        }
        */
        normalize();
        //s1s2Detection();
        //Детектируем основные тона
        sxDetection(false);
        System.out.println(mHisto.toString());
        System.out.println("Threshold: " + mHisto.getThreshold() + "\n");
    }
    
    
    private void calcParameters() {
        double semiWaveSquare = Double.MIN_VALUE;
        double prevIn = Double.MIN_VALUE;
        double in = Double.MIN_VALUE;
        double firstDer;
        double prevFirstDer = Double.MIN_VALUE;
        double secondDer;
        double prevSecondDer = Double.MIN_VALUE;
        boolean directionUp = true;
        boolean inflectionOut = true;
        
        int cntFromLastDirection = 0;
        int cntFromLastInflection = 0;
        
        for (HeartSoundPortion cur : data) {
            //in = cur.getOut();
            in = cur.getIn();
            firstDer = in - prevIn;
            secondDer = firstDer - prevFirstDer;
            
            cur.setFirstDerivative(firstDer);
            cur.setSecondDerivative(secondDer);
            
            boolean curDirectionUp = (firstDer >= Double.MIN_VALUE);
            cur.setChangeDirectionPoint(directionUp != curDirectionUp);
            if (cur.isChangeDirectionPoint()) {
                cntFromLastDirection = 0;
            } else {
                cntFromLastDirection++;
            }
            cur.setTimeFromChangeDirPoint((double)cntFromLastDirection);
            directionUp = curDirectionUp;
                
            boolean curInflectionOut = (secondDer >= Double.MIN_VALUE);
            cur.setInflectionPoint(inflectionOut != curInflectionOut);
            if (cur.isInflectionPoint()) {
                cntFromLastInflection = 0;
            } else {
                cntFromLastInflection++;
            }
            cur.setTimeFromInflectionPoint((double)cntFromLastInflection);
            inflectionOut = curInflectionOut;
            
            boolean newSemiWave = ((prevIn * in < Double.MIN_VALUE) || ((prevIn != Double.MIN_VALUE) && (in == Double.MIN_VALUE)));
            if (newSemiWave) {
                cur.setSquareSemiWave(semiWaveSquare);
                semiWaveSquare = Double.MIN_VALUE;
            } else {
                semiWaveSquare += Math.abs(in);
                cur.setSquareSemiWave(Double.MIN_VALUE);
            }
            prevIn = in;
            prevFirstDer = firstDer;
            prevSecondDer = secondDer;
        }
        
        semiWaveSquare += Math.abs(in);
        for (int i = data.size()-1; i >= 0; i--) {
            HeartSoundPortion cur = data.get(i);
            double semiWave = cur.getSquareSemiWave();
            
            if (semiWave == Double.MIN_VALUE) {
                cur.setSquareSemiWave(semiWaveSquare);
            } else {
                semiWaveSquare = semiWave;
            }
        }
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


    public void normalize() {
        for (HeartSoundPortion heartSoundPortion : data) {
            heartSoundPortion.normalize();
            mHisto.push(heartSoundPortion.getMagnitude());
            //mHisto.push(heartSoundPortion.getWindowEnergy());
            //mHisto.push(heartSoundPortion.getSquareSemiWave());
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
            //boolean remove = (sxNode.getMaxMagnitude() < THRESHOLD_MAGNITUDE);
            boolean remove = false;
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

    public Double getFreqStep() {
        return freqStep;
    }
}
