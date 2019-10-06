package nl.siwoc.mediainfo.riff;

public class ListChunk extends Chunk{

	private String listType;

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}
	
	public ListChunk() {
		setId("LIST");
	}
	
}
