package nl.siwoc.mediainfo.qtff;

public class TrakBox extends Box {

    private String handlerType;
	
    public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	public TrakBox(Box parent, int size, byte[] data) throws Exception {
		setType("trak");
		setSize(size);
		setParent(parent);
		parseChildren(this, size, data);
	}

	public String toString() {
		return "TrakBox{ " +
	            "handlerType=" + getHandlerType() + " }";
	}

}
