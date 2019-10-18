package nl.siwoc.mediainfo.qtff;

public abstract class SampleEntry extends Box {
	
	private short dataReferenceIndex;

	public short getDataReferenceIndex() {
		return dataReferenceIndex;
	}

	public void setDataReferenceIndex(short dataReferenceIndex) {
		this.dataReferenceIndex = dataReferenceIndex;
	}
	
}
