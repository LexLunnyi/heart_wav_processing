/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   WavReader.cpp
 * Author: aberdnikov
 * 
 * Created on September 13, 2018, 8:41 PM
 */

#include "WavReader.h"

TWavReader::TWavReader() {
    timeDataIndex = 0;
}


TWavReader::~TWavReader() {
}



bool TWavReader::init(string & wavPath, string & error) {
    //Open WAV-file
    filePath = wavPath;
    int fd = open (wavPath.c_str(), O_RDONLY);
    if (fd == -1) {
        error = "Error open wav-file";
        return false;
    }
    //Read header
    int ret = read(fd, &header, sizeof(TWavHeader));
    if (ret <= 0) {
        error = "Error read file";
        return false;
    }
    //Output wav header
    string res = "";
    headerToString(res);
    printf("%s", res.c_str());
    
    //Read data;
    uint16_t sample = 0;
    //uint16_t first = 0;
    unsigned int index = 0;
    unsigned int readCount = header.subchunkDataSize;
    
    while (readCount > 0) {
    
        ret = read(fd, &sample, header.blockAlign);
        if (ret <= 0) {
            error = "Error read file";
            return false;
        }
        index++;
        
        //first = sample & 0x00FF;
        //sample = (sample >> 8) | (first << 8);
        
        if (2 == header.blockAlign) sample += 32768;
        
        if (needFFT) {
            if (index >= POSITION_START) {
                if (!processFFT(sample)) break;
            }
        }
        
        //printf("%u; %u\n", index, sample);
        
        readCount -= header.blockAlign;
    }
    
    return true;
}




void TWavReader::headerToString(string & output) {
    output = "HEADER OF FILE " + filePath + ":\n";
    output += "    chunkId           -> "; output.append(header.chunkId, 4); output += "\n";  
    output += "    chunkSize         -> " + to_string(header.chunkSize) + "\n";
    output += "    format            -> "; output.append(header.format, 4); output += "\n";
    output += "    subchunkFmtId     -> "; output.append(header.subchunkFmtId, 4); output += "\n";
    output += "    subchunkFmtSize   -> " + to_string(header.subchunkFmtSize) + "\n";
    output += "    audioFormat       -> " + to_string(header.audioFormat) + "\n";
    output += "    numChannels       -> " + to_string(header.numChannels) + "\n";
    output += "    sampleRate        -> " + to_string(header.sampleRate) + "\n";
    output += "    byteRate          -> " + to_string(header.byteRate) + "\n";
    output += "    blockAlign        -> " + to_string(header.blockAlign) + "\n";
    output += "    bitsPerSample     -> " + to_string(header.bitsPerSample) + "\n";
    output += "    subchunkFactId    -> "; output.append(header.subchunkFactId, 4); output += "\n";
    output += "    subchunkFactSize  -> " + to_string(header.subchunkFactSize) + "\n";
    output += "    samplesPerChannel -> " + to_string(header.samplesPerChannel) + "\n";
    output += "    subchunkDataId    -> "; output.append(header.subchunkDataId, 4); output += "\n";
    output += "    subchunkDataSize  -> " + to_string(header.subchunkDataSize) + "\n";
    output += "\n";
}




void TWavReader::readData() {
    
}





bool TWavReader::processFFT(uint16_t sample) {
    timeData[timeDataIndex] = complex<double>(0., 0.);
    timeData[timeDataIndex] += sample;
    freqData[timeDataIndex] = timeData[timeDataIndex];
    timeDataIndex++;
    if (READ_SAMPLE_COUNT == timeDataIndex) {
        //If all frame was read
        FastFourierTransformer fft;
        fft.fft2(freqData, READ_SAMPLE_COUNT);
        timeDataIndex = 0;

        double curTime = 0.0;
        double curFreq = 0.0;
        
        double timeResolution = 1.0 / (double)header.sampleRate;
        double freqResolution = (double)header.sampleRate / (double)READ_SAMPLE_COUNT;
        
        for (unsigned int index = 0; index < READ_SAMPLE_COUNT; index++) {
            curTime = index * timeResolution;
            curFreq = index * freqResolution;

            //index; time; timeVal; freq; freqVal
            printf("%u;%.3f;%.3f;%.3f;%.3f\n", index, curTime, timeData[index].real(), curFreq, abs(freqData[index]));
        }
        return false;
    } else {
        return true;
    }
}

