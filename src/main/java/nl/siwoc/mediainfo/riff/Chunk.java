package nl.siwoc.mediainfo.riff;

public class Chunk {

	private String id;
	private int size;
	private byte[] data;
	private int loaded;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getLoaded() {
		return loaded;
	}

	public void setLoaded(int loaded) {
		this.loaded = loaded;
	}
}
