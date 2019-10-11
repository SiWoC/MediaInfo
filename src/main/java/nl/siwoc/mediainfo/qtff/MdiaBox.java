package nl.siwoc.mediainfo.qtff;

public class MdiaBox extends Box {
	
    public MdiaBox(Box parent, int size, byte[] data) throws Exception {
		setType("mdia");
		setSize(size);
		setParent(parent);
		parseChildren(this, size, data);
	}

	public String toString() {
		return "MdiaBox{}";
	}
    
}
