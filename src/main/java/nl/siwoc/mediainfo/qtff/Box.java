package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Box {

	protected static final Logger LOGGER = Logger.getLogger(Box.class.getName());
	
	private String type;
	private int size;
	private ArrayList<Box> children = new ArrayList<Box>();
	private Box parent = null;

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
	
	public Box getChild(String childTypeToGet) {
		for (Box child : children) {
			if (childTypeToGet.equals(child.getType())) {
				return child;
			}
		}
		return null;
	}
	
	public void addChild(Box child) {
		children.add(child);
	}

	public Box getParent() {
		return parent;
	}

	public void setParent(Box parent) {
		this.parent = parent;
	}

	protected void parseChildren(Box parent, int size, byte[] data) throws Exception {
		long remaining = size - 8;
		try (InputStream is = new ByteArrayInputStream(data)) {
			while (remaining >= 4) {
				int childSize = QTFFUtils.readIntBE(is);
				String childType = QTFFUtils.readFourCC(is);
				byte[] childData;
				remaining = remaining - childSize;
				LOGGER.info("Parent: [" + getType() + "] has child: [" + childType + "] with size [" + childSize + "]");
				boolean skipNeeded = true;
				String className = "nl.siwoc.mediainfo.qtff." + childType.substring(0, 1).toUpperCase() + childType.substring(1) + "Box";
				try {
					Class<?> clazz = Class.forName(className);
					childData = new byte[childSize - 8];
					is.read(childData);
					skipNeeded = false;
					Constructor<?> ctor = clazz.getConstructor(Box.class, int.class, byte[].class);
					addChild((Box) ctor.newInstance(new Object[] {parent, childSize, childData }));
				} catch (Exception e) {
					LOGGER.warning(e.getMessage());
					// unneeded/unsupported/unknown i.e. newInstance failed
					LOGGER.info("Unsupported childtype: [" + childType + "] className: " + className + " skipNeeded=" + skipNeeded);
					if (skipNeeded) {
						is.skip(childSize - 8);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected SampleEntry createSampleEntry(Box parent, int entrySize, String entryType, byte[] entryData) throws Exception {
		try {
			LOGGER.info("Parent: [" + getType() + "] has sampleEntry: [" + entryType + "] with size [" + entrySize + "]");
			String className = "nl.siwoc.mediainfo.qtff." + entryType.substring(0, 1).toUpperCase() + entryType.substring(1) + "SampleEntry";
			try {
				Class<?> clazz = Class.forName(className);
				Constructor<?> ctor = clazz.getConstructor(Box.class, int.class, byte[].class);
				return (SampleEntry)ctor.newInstance(new Object[] {parent, entrySize, entryData });
			} catch (Exception e) {
				// unneeded/unsupported/unknown i.e. newInstance failed
				LOGGER.info("Unsupported childtype: [" + entryType + "] className: " + className);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

    protected Box searchUp(String type) {
		Box up = getParent();
		LOGGER.finer("Searching up for [" + type + "]");
	    while (up != null) {
	    	LOGGER.finer("Found up [" + up.getType() + "]");
	    	if (up.getType().equals(type)) {
	    		return up;
	    	}
	    	up = up.getParent();
	    }
	    return null;
    }

    public void print(int i) {
		for (int k = 0 ; k <= i ; k++) {
			System.out.print("  ");
		}
		System.out.println("[" + getType() + "]");
		for (Box child : children) {
			child.print(i + 1);
		}
		
	}

	public String toString() {
		return "Box{ " +
	            "type=" + getType() + " }";
	}

	public ArrayList<Box> getChildren(String type) {
		ArrayList<Box> childrenOfType = new ArrayList<Box>();
		for (Box child : children) {
			if (child.getType().equals(type)) {
				childrenOfType.add(child);
			}
		}
		return childrenOfType;
	}

    
}
