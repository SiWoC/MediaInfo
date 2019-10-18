package nl.siwoc.mediainfo.qtff;

public class TrakBox extends Box {

    private String handlerType;
    private String codecId;
	private short width;
	private short height;
	private short channelCount;
	
    public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	public String getCodecId() {
		return codecId;
	}

	public void setCodecId(String codecId) {
		this.codecId = codecId;
	}

	public short getWidth() {
		return width;
	}

	public void setWidth(short width) {
		this.width = width;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public short getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(short channelCount) {
		this.channelCount = channelCount;
	}

	public TrakBox(Box parent, int size, byte[] data) throws Exception {
		setType("trak");
		setSize(size);
		setParent(parent);
		parseChildren(this, size, data);
	}

	public String toString() {
		return "TrakBox{ " +
	            "handlerType=" + getHandlerType() + 
	            ", codecId=" + getCodecId() + 
	            " }";
	}

}
