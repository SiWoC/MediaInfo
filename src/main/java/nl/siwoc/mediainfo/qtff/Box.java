/*******************************************************************************
 * Copyright (c) 2019 Niek Knijnenburg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.siwoc.mediainfo.utils.ReadUtils;

public class Box {
	
	protected static final Logger LOG = LoggerFactory.getLogger(Box.class);

	private String type;
	private long size;
	private ArrayList<Box> children = new ArrayList<Box>();
	private Box parent = null;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}
	
	public void setSize(long atomSize) {
		this.size = atomSize;
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
		if (child != null) {
			children.add(child);
		}
	}

	public Box getParent() {
		return parent;
	}

	public void setParent(Box parent) {
		this.parent = parent;
	}

	protected void parseChildren(Box parent, long size, byte[] data) throws Exception {
		long remaining = size - 8;
		try (InputStream is = new ByteArrayInputStream(data)) {
			while (remaining >= 4) {
				long childSize = ReadUtils.readUInt32BE(is);
				String childType = ReadUtils.readFourCC(is);
				byte[] childData;
				remaining = remaining - childSize;
				LOG.info("Parent: [" + getType() + "] has child: [" + childType + "] with size [" + childSize + "]");
				boolean skipNeeded = true;
				String className = "nl.siwoc.mediainfo.qtff." + childType.substring(0, 1).toUpperCase() + childType.substring(1) + "Box";
				try {
					Class<?> clazz = Class.forName(className);
					childData = new byte[(int) (childSize - 8)];
					is.read(childData);
					skipNeeded = false;
					Constructor<?> ctor = clazz.getConstructor(Box.class, long.class, byte[].class);
					addChild((Box) ctor.newInstance(new Object[] {parent, childSize, childData }));
				} catch (Exception e) {
					LOG.warn(e.getMessage());
					// unneeded/unsupported/unknown i.e. newInstance failed
					LOG.info("Unsupported childtype: [" + childType + "] className: " + className + " skipNeeded=" + skipNeeded);
					if (skipNeeded) {
						is.skip(childSize - 8);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected SampleEntry createSampleEntry(Box parent, long entrySize, String entryType, byte[] entryData) throws Exception {
		try {
			LOG.info("Parent: [" + getType() + "] has sampleEntry: [" + entryType + "] with size [" + entrySize + "]");
			// ac-3 > Ac3SampleEntry
			entryType = entryType.replace("-", "");
			String className = "nl.siwoc.mediainfo.qtff." + entryType.substring(0, 1).toUpperCase() + entryType.substring(1) + "SampleEntry";
			try {
				Class<?> clazz = Class.forName(className);
				Constructor<?> ctor = clazz.getConstructor(Box.class, long.class, byte[].class);
				return (SampleEntry)ctor.newInstance(new Object[] {parent, entrySize, entryData });
			} catch (Exception e) {
				// unneeded/unsupported/unknown i.e. newInstance failed
				LOG.info("Unsupported childtype: [" + entryType + "] className: " + className);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

    protected Box searchUp(String type) {
		Box up = getParent();
		LOG.trace("Searching up for [" + type + "]");
	    while (up != null) {
	    	LOG.trace("Found up [" + up.getType() + "]");
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
		addChildrenOfType(childrenOfType, type);
		return childrenOfType;
	}
		
	public ArrayList<Box> addChildrenOfType(ArrayList<Box> childrenOfType, String type) {
		for (Box child : children) {
			if (child.getType().equals(type)) {
				childrenOfType.add(child);
			}
			child.addChildrenOfType(childrenOfType, type);
		}
		return childrenOfType;
	}

    
}
