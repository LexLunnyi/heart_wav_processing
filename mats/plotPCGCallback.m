function [] = plotPCGCallback(src,event)
global indata;
global index;
global OUT;
global hFig;

function saveMarkUp()
    newFileName = strcat("matdata/", OUT.PATH(index), "/", OUT.FILE(index), ".mat");
    path = strcat("matdata/", OUT.PATH(index));
    MARKUP = indata;
    if ~exist(path, 'dir')
        % Folder does not exist so create it.
        mkdir(path);
    end
    save(newFileName, "MARKUP");
    OUT.PROCESSED(index) = 1;
    save("matdata/OUT.mat", "OUT");
    close(hFig);
    clear all;
end


    % BEGIN OF plotPCGCallback FUNCTION
    curKey = event.Key;
    if strcmp(curKey,'shift')
        answer = questdlg('Are you sure you want to save markup?', 'Dessert Menu', 'OK', 'Cancel', 'Cancel');
        if strcmp(answer,'OK')
            saveMarkUp();
        end
    else
        [x,y,button] = ginput(2);
        first = int32(x(1));
        second = int32(x(2));
        indata.MANUAL([first:second])=1.0;
    end
end