import numpy as np
import matplotlib.pyplot as plt
import os



class Options():
    NORMAL_PATH = '/opt/data/output/normal/'
    ABNORMAL_PATH = '/opt/data/output/abnormal/'


class Columns():
    TIME = 0
    SIGNAL = 1
    MAGNITUDE = 2
    SX = 3
    S1 = 4
    S2 = 5



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
    
    def makeSx(self):
        myFile = np.genfromtxt(self.csv, delimiter=';')
        plt.rcParams["figure.figsize"] = (20,10)
        plt.rcParams["legend.loc"] = 'upper right'
        plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SIGNAL], label='source')
        plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.MAGNITUDE], label='magnitude')
        plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.SX], label='Sx')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.S1], label='S1')
        #plt.plot(myFile[:,Columns.TIME], myFile[:, Columns.S2], label='S2')

        plt.legend()
        plt.savefig(self.png)
        plt.clf()


for file in os.listdir(Options.NORMAL_PATH):
    if file.endswith(".csv"):
        fig = Figure(Options.NORMAL_PATH, file)
        fig.makeSx()

for file in os.listdir(Options.ABNORMAL_PATH):
    if file.endswith(".csv"):
        fig = Figure(Options.ABNORMAL_PATH, file)
        fig.makeSx()