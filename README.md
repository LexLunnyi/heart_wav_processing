# heart-wav-processing
## Description

This application is intended to process the heart's sounds:
* detect s1 and s2 sounds;
* determine some important parameters of sounds (frequency, duration, magnitude and so on);
* make diagnostic information by got parameters.

## Using

Building:
mvn install

Running:
java -jar heart-sound-recognition.jar sound.wav

Example:
java -jar heart-sound-recognition-0.0.1-SNAPSHOT.jar ../wavs/EAS.wav 
