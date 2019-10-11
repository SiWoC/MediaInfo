package nl.siwoc.mediainfo.qtff;

import java.util.HashMap;

public class Box {

	private String type;
	private int size;
	private HashMap<String,Box> childBoxes = new HashMap<String,Box>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public Box getChild(String type) {
		return childBoxes.get(type);
	}
	
	public void putChild(String type, Box child) {
		childBoxes.put(type, child);
	}

}
