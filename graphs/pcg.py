import numpy as np
import matplotlib.pyplot as plt
import os



class Options():
    PATH = '/opt/data_debug/output/'


class Columns():
    TIME                         = 0
    SIGNAL                       = 1
    FILTRED                      = 2
    MAGNITUDE                    = 3
    SX                           = 4
    MAX_HARMONIC_INDEX           = 5
    SQUARE_SEMI_WAVE             = 6
    WINDOWS_ENERGY               = 7
    TIME_FROM_CHANGE_POINT       = 8
    TIME_FROM_INFLECTION_POINT   = 9
    MAGNITUDES_ANGLE             = 10
    WINDOW_CHANGE_POINTS_CNT     = 11
    WINDOW_INFLECTION_POINTS_CNT = 12
    FIRST_DERIVATIVE             = 13
    SECOND_DERIVATIVE            = 14


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
        plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.FILTRED], label='FILTRED')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.MAGNITUDE], label='MAGNITUDE')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SX], label='SX')
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
