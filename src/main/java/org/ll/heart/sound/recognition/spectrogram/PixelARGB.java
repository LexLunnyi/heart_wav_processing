package org.ll.heart.sound.recognition.spectrogram;


/**
 *
 * @author Aleksey.Berdnikov
 */
public class PixelARGB {
    private int alpha;
    private int red;
    private int green;
    private int blue;
    private int pixel;
    
    public PixelARGB(int alpha, int red, int green, int blue) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
        compile();
    }
    
    public PixelARGB(int pixel) {
        this.pixel = pixel;
        
        alpha = (pixel >> 24) & 0xff;
        red = (pixel >> 16) & 0xff;
        green = (pixel >> 8) & 0xff;
        blue = (pixel) & 0xff;
    }

    public int getPixel() {
        return pixel;
    }
    
    public int getAlpha() {
        return alpha;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
    
    private void compile() {
        pixel = (alpha & 0xff) << 24;
        pixel |= (red & 0xff) << 16;
        pixel |= (green & 0xff) << 8;
        pixel |= (blue & 0xff);
    }
    
    public void makeBlackWhite() {
        int val = (int)((0.3 * (double)red) + (0.59 * (double)green) + (0.11 * (double)blue));
        alpha = 255;
        red = val;
        green = val;
        blue = val;
        compile();
    } 
}