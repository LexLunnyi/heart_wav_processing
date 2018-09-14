#include "main.h"


int main (int argc, char* argv[]) {
    TWavReader wavReader;
    
    printf("WAV PROCESSOR STARTED\n");
    
    if (argc < 2) {
        showHelp();
        return 0;
    }
    
    string path(argv[1]);
    printf("    path to wav-file -> %s\n", path.c_str());
    
    string error = "";
    if (!wavReader.init(path, error)) {
        printf("    error init TWavReader -> %s\n", error.c_str());
        return 0;
    }
    
    return 0;
}



void showHelp() {
    printf("\n");
    printf("Example:\n");
    printf("    ./wav_processor path_to_wav/processing.wav\n");
    printf("\n");
}
