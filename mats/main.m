global indata;
global index;
global OUT;

load("matdata/OUT.mat");

index = 0;

for i = 1:length(OUT.INDEX)
    processed = OUT.PROCESSED(i);
    if processed == 0
        index = i;
        break
    end
end

if index > 0
    fileName = strcat("data/", OUT.PATH(index), "/", OUT.FILE(index));
    indata = readtable(fileName);
    indata.MANUAL = zeros(length(indata.SIGNAL), 1);
    plot_pcg();
end