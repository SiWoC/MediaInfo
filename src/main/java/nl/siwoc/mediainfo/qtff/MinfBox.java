package nl.siwoc.mediainfo.qtff;

public class MinfBox extends Box {
	
    public MinfBox(Box parent, int size, byte[] data) throws Exception {
		setType("minf");
		setSize(size);
		setParent(parent);
		parseChildren(this, size, data);
	}

	public String toString() {
		return "MinfBox{}";
	}

}
