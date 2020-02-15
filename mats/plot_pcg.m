function [] = plot_pcg()
global indata;
global hFig;

hFig = figure;
hFig.WindowState = 'maximized';
plot(indata.SIGNAL);
hold on
plot(indata.MANUAL, 'YDataSource', 'indata.MANUAL');
addlistener ( hFig, 'WindowKeyPress', @plotPCGCallback);
linkdata on

end