#include "main.h"


int main (int argc, char* argv[]) {
    printf("WAV PROCESSOR STARTED\n");
    
    if (argc < 2) {
        showHelp();
        return 0;
    }
    
    WavData data;
    string act(argv[1]);
    if (act == "filter") {
        string path(argv[2]);
        filter(path, data);
    } else if (act == "test") {
        test();
    } else {
        showHelp();
    }
    

    WaveDisplay waveDisplay(&data);
    usleep(100000);
    while (waveDisplay.running) {
        usleep(100000);
    }
    
    
    return 0;
}




void filter(string & wavPath, WavData & data) {
    TWavReader wavReader;
    string error = "";
    if (!wavReader.init(wavPath, data, error)) {
        printf("    error init TWavReader -> %s\n", error.c_str());
        return ;
    }    
}




void test() {
    TesterFFT testFFT;
    testFFT.process();
}



void showHelp() {
    printf("\n");
    printf("Example:\n");
    printf("    ./wav_processor filter path_to_wav/processing.wav\n");
    printf("\n");
}




// 3) Рефакторинг класса Display