package nl.siwoc.mediainfo.qtff;

public class StblBox extends Box {
	
    public StblBox(Box parent, int size, byte[] data) throws Exception {
		setType("stbl");
		setSize(size);
		setParent(parent);
		parseChildren(this, size, data);
	}

	public String toString() {
		return "StblBox{}";
	}
    
}
