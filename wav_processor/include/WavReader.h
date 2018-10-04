#ifndef WAVREADER_H
#define WAVREADER_H

#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/types.h>
#include <unistd.h>
#include <complex.h>



#include "FastFourierTransformer.h"

using namespace std;


struct TWavHeader {
    //"RIFF" chars in ASCII
    char chunkId[4];
    //File size without size of chunkId and chunkSize (minus 8)
    unsigned int chunkSize;
    //"WAVE" chars in big-endian (0x57415645)
    char format[4];
    //"fmt " chars in big-endian (0x666d7420)
    char subchunkFmtId[4];
    //Remaining size of fmt field
    unsigned int subchunkFmtSize;
    //Format of audio file (for PCM value is 1)
    unsigned short audioFormat;
    //Number of channels (Mono = 1; Stereo = 2)
    unsigned short numChannels;
    //Sampling frequency. 8000 Hz, 44100 Hz and etc.
    unsigned int sampleRate;
    //sampleRate * numChannels * bitsPerSample/8
    unsigned int byteRate;
    //numChannels * bitsPerSample/8
    unsigned short blockAlign;
    //Ыound depth. 8 bit, 16 бbit and etc.
    unsigned short bitsPerSample;
    //"fact " chars
    char subchunkFactId[4];
    //Remaining size of fact field
    unsigned int subchunkFactSize;
    //Samples per channel
    unsigned int samplesPerChannel;
    //"data " chars in big-endian (0x64617461)
    char subchunkDataId[4];
    //Remaining size of data field (numSamples * numChannels * bitsPerSample/8)
    unsigned int subchunkDataSize;
};



class TWavReader {
private:
    TWavHeader header;
    
    string filePath;
    
    //static const unsigned int READ_SAMPLE_COUNT = 4096;
    //static const unsigned int POSITION_START    = 0;
    static const unsigned int READ_SAMPLE_COUNT = 1024;
    static const unsigned int POSITION_START    = 3000;

    void headerToString(string & output);
    void readData();
    
    //vars for FFT
    static const bool needFFT = true;
    complex<double> timeData[READ_SAMPLE_COUNT];
    complex<double> freqData[READ_SAMPLE_COUNT];
    unsigned int timeDataIndex = 0;
public:
    TWavReader();
    virtual ~TWavReader();
    
    bool init(string & wavPath, string & error);
    bool processFFT(uint16_t sample);
};









/*
 
 
   FILE *file;
    errno_t err;
    err = fopen_s(&file, "Slipknot - Three Nil.wav", "rb");
    if (err)
    {
        printf_s("Failed open file, error %d", err);
        return 0;
    }

    WAVHEADER header;

    fread_s(&header, sizeof(WAVHEADER), sizeof(WAVHEADER), 1, file);

    // Выводим полученные данные
    printf_s("Sample rate: %d\n", header.sampleRate);
    printf_s("Channels: %d\n", header.numChannels);
    printf_s("Bits per sample: %d\n", header.bitsPerSample);

    // Посчитаем длительность воспроизведения в секундах
    float fDurationSeconds = 1.f * header.subchunk2Size / (header.bitsPerSample / 8) / header.numChannels / header.sampleRate;
    int iDurationMinutes = (int)floor(fDurationSeconds) / 60;
    fDurationSeconds = fDurationSeconds - (iDurationMinutes * 60);
    printf_s("Duration: %02d:%02.f\n", iDurationMinutes, fDurationSeconds);

    fclose(file);

    _getch();
return 0;
 
 
 */





#endif /* WAVREADER_H */

