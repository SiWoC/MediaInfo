package nl.siwoc.mediainfo.qtff;

import java.util.Arrays;

/**
 * Created by yanshun.li on 10/24/16.
 */

public class MvhdBox extends FullBox {

    public long creationTime;
    public long modificationTime;
    public int timescale;
    public long duration;

    public int rate;
    public int volume;
    public int reserved = 0;
    public int[] matrix = new int[9];
    public int[] preDefined = new int[6];
    public int nextTrackId;

    public MvhdBox(int size, byte[] childData) {
        if (getVersion() == 1) {
        } else {
        }
    }

    @Override
    public String toString() {
        return "MvhdBox{" +
                "creationTime=" + creationTime +
                ", modificationTime=" + modificationTime +
                ", timescale=" + timescale +
                ", duration=" + duration +
                ", rate=" + rate +
                ", volume=" + volume +
                ", reserved=" + reserved +
                ", matrix=" + Arrays.toString(matrix) +
                ", preDefined=" + Arrays.toString(preDefined) +
                ", nextTrackId=" + nextTrackId +
                '}';
    }
}
