function [] = plot_pcg()
global indata;

hFig = figure;
plot(indata.SIGNAL);
hold on
plot(indata.MANUAL, 'YDataSource', 'indata.MANUAL');
addlistener ( hFig, 'WindowKeyPress', @plotPCGCallback);
linkdata on
end
