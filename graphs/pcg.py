import numpy as np
import matplotlib.pyplot as plt
import os



class Options():
    NORMAL_PATH = '/opt/data/output/normal/'
    ABNORMAL_PATH = '/opt/data/output/abnormal/'


class Columns():
    TIME                         = 0
    SIGNAL                       = 1
    MAGNITUDE                    = 2
    SX                           = 3
    MAX_HARMONIC_INDEX           = 4
    SQUARE_SEMI_WAVE             = 5
    WINDOWS_ENERGY               = 6
    TIME_FROM_CHANGE_POINT       = 7
    TIME_FROM_INFLECTION_POINT   = 8
    MAGNITUDES_ANGLE             = 9
    WINDOW_CHANGE_POINTS_CNT     = 10
    WINDOW_INFLECTION_POINTS_CNT = 11
    FIRST_DERIVATIVE             = 12
    SECOND_DERIVATIVE            = 13


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
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.MAGNITUDE], label='MAGNITUDE')
        plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SX], label='SX')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.MAX_HARMONIC_INDEX], label='MAX_HARMONIC_INDEX')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SQUARE_SEMI_WAVE], label='SQUARE_SEMI_WAVE')
        plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.WINDOWS_ENERGY], label='WINDOWS_ENERGY')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.TIME_FROM_CHANGE_POINT], label='TIME_FROM_CHANGE_POINT')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.TIME_FROM_INFLECTION_POINT], label='TIME_FROM_INFLECTION_POINT')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.MAGNITUDES_ANGLE], label='MAGNITUDES_ANGLE')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.WINDOW_CHANGE_POINTS_CNT], label='WINDOW_CHANGE_POINTS_CNT')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.WINDOW_INFLECTION_POINTS_CNT], label='WINDOW_INFLECTION_POINTS_CNT')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.FIRST_DERIVATIVE], label='FIRST_DERIVATIVE')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SECOND_DERIVATIVE], label='SECOND_DERIVATIVE')

        plt.legend()
        plt.savefig(self.png)
        plt.clf()


for file in os.listdir(Options.NORMAL_PATH):
    if file.endswith(".csv"):
        fig = Figure(Options.NORMAL_PATH, file)
        fig.make_graphs()

for file in os.listdir(Options.ABNORMAL_PATH):
    if file.endswith(".csv"):
        fig = Figure(Options.ABNORMAL_PATH, file)
        fig.make_graphs()