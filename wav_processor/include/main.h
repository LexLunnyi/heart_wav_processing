#include <string>

#include "display.h"
#include "WavReader.h"
#include "WavData.h"
#include "TesterFFT.h"

using namespace std;


int main(int argc, char* argv[]);
void filter(string & wavPath, WavData & data);
void test();
void showHelp();