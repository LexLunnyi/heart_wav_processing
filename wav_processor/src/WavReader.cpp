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
    return true;
}




void TWavReader::headerToString(string & output) {
    output = "HEADER OF FILE " + filePath + ":\n";
    output += "    chunkId          -> "; output.append(header.chunkId, 4); output += "\n";  
    output += "    chunkSize        -> " + to_string(header.chunkSize) + "\n";
    output += "    format           -> "; output.append(header.format, 4); output += "\n";
    output += "    subchunkFmtId    -> "; output.append(header.subchunkFmtId, 4); output += "\n";
    output += "    subchunkFmtSize  -> " + to_string(header.subchunkFmtSize) + "\n";
    output += "    audioFormat      -> " + to_string(header.audioFormat) + "\n";
    output += "    numChannels      -> " + to_string(header.numChannels) + "\n";
    output += "    sampleRate       -> " + to_string(header.sampleRate) + "\n";
    output += "    byteRate         -> " + to_string(header.byteRate) + "\n";
    output += "    blockAlign       -> " + to_string(header.blockAlign) + "\n";
    output += "    bitsPerSample    -> " + to_string(header.bitsPerSample) + "\n";
    output += "    subchunkDataId   -> "; output.append(header.subchunkDataId, 4); output += "\n";
    output += "    subchunkDataSize -> " + to_string(header.subchunkDataSize) + "\n";
    output += "\n";
}

