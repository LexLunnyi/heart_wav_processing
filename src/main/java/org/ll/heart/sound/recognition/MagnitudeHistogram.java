package org.ll.heart.sound.recognition;

/**
 * !!!! FOR NORMALIZED VALUES ONLY !!!!
 */
public class MagnitudeHistogram {
    //Количество счетчиков в гистограмме
    private final int size;
    //Массив со счетчиками
    private final int counters[];
    //Количество проанализированных данных
    private int count = 0;
    
    //В конструкторе указываем сколько счетчиков будет в гистограмме
    public MagnitudeHistogram(int size) {
        this.size = size;
        this.counters = new int[size];
    }
    
    //Функция добавления для статистики нового элемента
    public void push(double magnitude) {
        count++;
        //Равномерно проходим по всем счетчикам
        for (int i = size-1; i >= 0; i--) {
            double threshold = (double)i/(double)size;
            //Если значение элемента превышает границу счетчика, то инкрементируем счетчик
            if (magnitude >= threshold) {
                counters[i]++;
                break;
            }
        }
    }

    //Функция рассчитывает пороговое значение по данным гистограммы
    public double getThreshold() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            //Определяем сколько елементов добавлено в первые i счетчиков
            sum += counters[i];
            //Если в процентном соотношении оно превышает порог
            if ((double)sum/(double)count >= 0.8D) {
                //то возращаем граничное значение счетчика,
                //оно и будет разделять все отсчеты сигнала на 2 группы
                return (double)(i+1)/(double)size;
            }
        }
        return 0.0D;
    }
    

    @Override
    public String toString() {
        String res = "MagnitudeHistogram{" + "size=" + size + ", counters: \n";
        for (int i = 0; i < size; i++) {
            res += i + ";" + counters[i] + ";\n";
        }
        res += "}\n";
        return res;
    }
}
