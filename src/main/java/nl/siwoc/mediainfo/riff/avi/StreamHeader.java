package nl.siwoc.mediainfo.riff.avi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.siwoc.mediainfo.riff.Chunk;
import nl.siwoc.mediainfo.riff.RIFFUtils;

public class StreamHeader extends Chunk{

	private static final Logger LOGGER = Logger.getLogger(StreamHeader.class.getName());
	
	private String type;
	private String handler;
	private int flags;
	private int priorityLanguage;
	private int initialFrames;
	private int scale;
	private int rate;
	private int start;
	private int length;
	private int suggestedBufferSize;
	private int quality;
	private int sampleSize;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int Flags) {
		this.flags = Flags;
	}

	public int getPriorityLanguage() {
		return priorityLanguage;
	}

	public void setPriorityLanguage(int priorityLanguage) {
		this.priorityLanguage = priorityLanguage;
	}

	public int getInitialFrames() {
		return initialFrames;
	}

	public void setInitialFrames(int InitialFrames) {
		this.initialFrames = InitialFrames;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int Scale) {
		this.scale = Scale;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int Rate) {
		this.rate = Rate;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int Start) {
		this.start = Start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int Length) {
		this.length = Length;
	}

	public int getSuggestedBufferSize() {
		return suggestedBufferSize;
	}

	public void setSuggestedBufferSize(int SuggestedBufferSize) {
		this.suggestedBufferSize = SuggestedBufferSize;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int Quality) {
		this.quality = Quality;
	}

	public int getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(int SampleSize) {
		this.sampleSize = SampleSize;
	}

	public StreamHeader(int size, byte[] data) throws Exception{
		setId("strh");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(data)){
			setType(RIFFUtils.readFourCC(is));
			setHandler(RIFFUtils.readFourCC(is));
			setFlags(RIFFUtils.readIntLE(is));
			setPriorityLanguage(RIFFUtils.readIntLE(is));
			setInitialFrames(RIFFUtils.readIntLE(is));
			setScale(RIFFUtils.readIntLE(is));
			setRate(RIFFUtils.readIntLE(is));
			setStart(RIFFUtils.readIntLE(is));
			setLength(RIFFUtils.readIntLE(is));
			setSuggestedBufferSize(RIFFUtils.readIntLE(is));
			setQuality(RIFFUtils.readIntLE(is));
			setSampleSize(RIFFUtils.readIntLE(is));
			//rcFrame rectangle follows, ignored

			LOGGER.log(Level.FINE,"Stream Header (strh):" + System.lineSeparator() +
				"   type=[" + type + "]" + System.lineSeparator() +
				"   handler=[" + handler + "]" + System.lineSeparator() +
				"   flags=" + flags + System.lineSeparator() +
				"   wPriority,wLanguage=" + priorityLanguage + System.lineSeparator() +
				"   initialFrames=" + initialFrames + System.lineSeparator() +
				"   scale=" + scale + System.lineSeparator() +
				"   rate=" + rate + System.lineSeparator() +
				"   start=" + start + System.lineSeparator() +
				"   length=" + length + System.lineSeparator() +
				"   suggestedBufferSize=" + suggestedBufferSize + System.lineSeparator() +
				"   quality=" + quality + System.lineSeparator() +
				"   sampleSize=" + sampleSize);
		} catch (Exception e) {
			throw e;
		}
	}

}
