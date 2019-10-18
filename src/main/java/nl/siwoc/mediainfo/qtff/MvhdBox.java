package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MvhdBox extends FullBox {

    private long creationTime;
    private long modificationTime;
    private int timescale;
    private long duration;

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

	public int getTimescale() {
		return timescale;
	}

	public void setTimescale(int timescale) {
		this.timescale = timescale;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public MvhdBox(Box parent, int size, byte[] data) throws Exception {
		setType("mvhd");
		setSize(size);
		setParent(parent);
		LOGGER.info("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(QTFFUtils.readFlag(is));
	        if (getVersion() == 1) {
	        	setCreationTime(QTFFUtils.readWideBE(is));
	        	setModificationTime(QTFFUtils.readWideBE(is));
	        	setTimescale(QTFFUtils.readIntBE(is));
	        	setDuration(QTFFUtils.readWideBE(is));
	        } else {
	        	setCreationTime(QTFFUtils.readIntBE(is));
	        	setModificationTime(QTFFUtils.readIntBE(is));
	        	setTimescale(QTFFUtils.readIntBE(is));
	        	setDuration(QTFFUtils.readIntBE(is));
	        }
	        LOGGER.info(toString());
	        // don't need rest at this moment
		} catch (Exception e){
			throw e;
		}
    }

	public String toString() {
		return "MvhdBox{" +
	            "version=" + getVersion() +
	            ", creationTime=" + getCreationTime() +
	            ", modificationTime=" + getModificationTime() +
	            ", timescale=" + getTimescale() +
	            ", duration=" + getDuration() +
	            " }";

	}
}
