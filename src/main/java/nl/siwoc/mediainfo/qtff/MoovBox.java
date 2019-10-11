package nl.siwoc.mediainfo.qtff;

public class MoovBox extends Box {
	
    public MoovBox(Box parent, int size, byte[] data) throws Exception {
		setType("moov");
		setSize(size);
		setParent(parent);
		parseChildren(this, size, data);
	}

	public String toString() {
		return "MoovBox{}";
	}
    
}
