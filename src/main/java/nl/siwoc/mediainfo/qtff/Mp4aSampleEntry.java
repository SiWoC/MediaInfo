package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Mp4aSampleEntry extends SampleEntry {
	
	private short channelCount;
	private short sampleSize;
	private int sampleRate;
	
	public short getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(short channelCount) {
		this.channelCount = channelCount;
	}

	public short getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(short sampleSize) {
		this.sampleSize = sampleSize;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	public Mp4aSampleEntry(Box parent, int size, byte[] data) throws Exception {
		setType("mp4a");
		setSize(size);
		setParent(parent);
		LOGGER.info("Creating " + getType());
        TrakBox trak = (TrakBox)searchUp("trak");
        if (trak != null) {
        	trak.setCodecId(getType());
        }
		try (InputStream is = new ByteArrayInputStream(data)){
			// SampleEntry base
			// skip 6
			QTFFUtils.readIntBE(is);
			QTFFUtils.readShortBE(is);
			setDataReferenceIndex(QTFFUtils.readShortBE(is));
			// VisualSampleEntry
			// skip 8 (0)
			QTFFUtils.readIntBE(is);
			QTFFUtils.readIntBE(is);
			setChannelCount(QTFFUtils.readShortBE(is));
			setSampleSize(QTFFUtils.readShortBE(is));
			QTFFUtils.readShortBE(is);
			setSampleRate(QTFFUtils.readIntBE(is));
			if (trak != null) {
				trak.setChannelCount(getChannelCount());
			}
			LOGGER.info(toString());
			
		} catch (Exception e) {
			throw e;
		}
	}

	public String toString() {
		return "Mp4aSampleEntry{ " +
	            "channelCount=" + getChannelCount() +
	            ", sampleSize=" + getSampleSize() +
	            ", sampleRate=" + getSampleRate() +
	            " }";
	}
}
