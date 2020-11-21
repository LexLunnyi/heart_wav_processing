package org.ll.heart.sound.recognition;

/**
 *
 * @author aberdnikov
 */
public class HeartSoundCategory {
    private int index;
    private String name;
    private String path;
    private String tag;

    public HeartSoundCategory(int index, String name, String path, String tag) {
        this.index = index;
        this.name = name;
        this.path = path;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIndex() {
        return index;
    }
}