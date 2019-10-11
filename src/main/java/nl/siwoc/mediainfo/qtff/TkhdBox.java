package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TkhdBox extends FullBox {

    private long creationTime;
    private long modificationTime;
    private int trackId;
    private long duration;
    private short layer;
    private short alternateGroup;
    private short volume;
    private int[] matrix = new int[9];
    private int width;
    private int height;    

    public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(long modificationTime) {
		this.modificationTime = modificationTime;
	}

	public int getTrackId() {
		return trackId;
	}

	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public short getLayer() {
		return layer;
	}

	public void setLayer(short layer) {
		this.layer = layer;
	}

	public short getAlternateGroup() {
		return alternateGroup;
	}

	public void setAlternateGroup(short alternateGroup) {
		this.alternateGroup = alternateGroup;
	}

	public short getVolume() {
		return volume;
	}

	public void setVolume(short volume) {
		this.volume = volume;
	}

	public int[] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[] matrix) {
		this.matrix = matrix;
	}

	private void setMatrix(int i, int k) {
		this.matrix[i] = k;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public TkhdBox(int size, byte[] data) throws Exception {
		setType("tkhd");
		setSize(size);
		LOGGER.info("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(QTFFUtils.readFlag(is));
	        if (getVersion() == 1) {
	        	setCreationTime(QTFFUtils.readWideBE(is));
	        	setModificationTime(QTFFUtils.readWideBE(is));
	        	setTrackId(QTFFUtils.readIntBE(is));
	        	QTFFUtils.readIntBE(is); //reserved
	        	setDuration(QTFFUtils.readWideBE(is));
	        } else {
	        	setCreationTime(QTFFUtils.readIntBE(is));
	        	setModificationTime(QTFFUtils.readIntBE(is));
	        	setTrackId(QTFFUtils.readIntBE(is));
	        	QTFFUtils.readIntBE(is); //reserved
	        	setDuration(QTFFUtils.readIntBE(is));
	        }
        	QTFFUtils.readIntBE(is); //reserved [0]
        	QTFFUtils.readIntBE(is); //reserved [1]
        	setLayer(QTFFUtils.readShortBE(is));
        	setAlternateGroup(QTFFUtils.readShortBE(is));
        	setVolume(QTFFUtils.readShortBE(is));
        	QTFFUtils.readShortBE(is); // reserved
        	for (int i = 0 ; i < matrix.length ; i++) {
        		setMatrix(i,QTFFUtils.readIntBE(is));
        	}
        	setWidth(QTFFUtils.readIntBE(is));
        	setHeight(QTFFUtils.readIntBE(is));
	        LOGGER.info(toString());
	        // don't need rest at this moment
		} catch (Exception e){
			throw e;
		}
    }

	public String toString() {
		return "TkhdBox{" +
	            "version=" + getVersion() +
	            ", creationTime=" + getCreationTime() +
	            ", modificationTime=" + getModificationTime() +
	            ", trackId=" + getTrackId() +
	            ", duration=" + getDuration() +
	            ", layer=" + getLayer() +
	            ", altgroup=" + getAlternateGroup() +
	            ", volume=" + getVolume() +
	            ", width=" + getWidth() +
	            ", height=" + getHeight() +
	            " }";
	}
}
