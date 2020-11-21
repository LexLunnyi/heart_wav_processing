import numpy as np
import matplotlib.pyplot as plt
import os



class Options():
    PATH = '/opt/data_debug/output/'


class Columns():
    ID                           = 0
    TIME                         = 1
    SIGNAL                       = 2
    FILTRED                      = 3
    MAGNITUDE                    = 4
    MFREQ                        = 5
    STAT_MAGNITUDE_MEAN          = 6
    STAT_MAGNITUDE_SD            = 7
    STAT_MFREQ_MEAN              = 8
    STAT_MFREQ_SD                = 9
    THRESHOLD_HISTOGRAM          = 10
    SX                           = 11

class Figure():
    path = ''
    name = 'noname'
    csv = ''
    png = ''
    
    def __init__(self, path, name):
        self.path = path
        self.name = name
        self.csv = os.path.join(path, file)
        self.png = os.path.join(path, file+'.png')
    
    def make_graphs(self):
        myFile = np.genfromtxt(self.csv, delimiter=';')
        plt.rcParams["figure.figsize"] = (20,10)
        plt.rcParams["legend.loc"] = 'upper right'
        
        plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SIGNAL], label='SIGNAL')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.FILTRED], label='FILTRED')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.MAGNITUDE], label='MAGNITUDE')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.STAT_MAGNITUDE_MEAN], label='STAT_MAGNITUDE_MEAN')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.STAT_MAGNITUDE_SD], label='STAT_MAGNITUDE_SD')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.STAT_SD], label='STAT_SD')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.M_FREQ], label='M_FREQ')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.THRESHOLD_HISTOGRAM], label='THRESHOLD_HISTOGRAM')
        plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SX], label='SX')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.MAX_HARMONIC_INDEX], label='MAX-HARMONIC-INDEX')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SQUARE_SEMI_WAVE], label='SQUARE-SEMI-WAVE')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.WINDOWS_ENERGY], label='WINDOWS-ENERGY')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.TIME_FROM_CHANGE_POINT], label='TIME-FROM-CHANGE-POINT')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.TIME_FROM_INFLECTION_POINT], label='TIME-FROM-INFLECTION-POINT')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.MAGNITUDES_ANGLE], label='MAGNITUDES-ANGLE')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.WINDOW_CHANGE_POINTS_CNT], label='WINDOW-CHANGE-POINTS-CNT')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.WINDOW_INFLECTION_POINTS_CNT], label='WINDOW-INFLECTION-POINTS-CNT')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.FIRST_DERIVATIVE], label='FIRST-DERIVATIVE')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SECOND_DERIVATIVE], label='SECOND-DERIVATIVE')

        plt.legend()
        plt.savefig(self.png)
        plt.clf()


for item in os.listdir(Options.PATH):
    item = os.path.join(Options.PATH, item)
    if os.path.isdir(item):
        for file in os.listdir(item):
            if file.endswith(".csv"):
                fig = Figure(item, file)
                fig.make_graphs()
