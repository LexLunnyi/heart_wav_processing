package org.ll.heart.sound.recognition;

import java.util.Date;

/**
 *
 * @author aberdnikov
 */
public class Normalizer {
    final SignalPortion max = new SignalPortion(0, new Date(0), 0, null);
    
    void calc(SignalPortion cur) {
        max.setSource(Math.max(max.getSource(), cur.getSource()));
        max.setFiltered(Math.max(max.getFiltered(), cur.getFiltered()));
        max.setMagnitude(Math.max(max.getMagnitude(), cur.getMagnitude()));
        max.setMfreq(Math.max(max.getMfreq(), cur.getMfreq()));
    }
    
    void norm(SignalPortion cur) {
        cur.setSource(cur.getSource() / max.getSource());
        cur.setFiltered(cur.getFiltered() / max.getFiltered());
        cur.setMagnitude(cur.getMagnitude() / max.getMagnitude());
        cur.setMfreq(cur.getMfreq() / max.getMfreq());
    }
}
