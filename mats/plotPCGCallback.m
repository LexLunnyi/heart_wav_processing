function [] = plotPCGCallback(src,event)
global indata;
[x,y,button] = ginput(2);
first = int32(x(1));
second = int32(x(2));
indata.MANUAL([first:second])=1.0;
end
