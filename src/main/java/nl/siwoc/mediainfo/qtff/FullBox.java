package nl.siwoc.mediainfo.qtff;

public class FullBox extends Box {
    private int version;
    private byte[] flag;
	
	public int getVersion() {
		return version;
	}
	
    public void setVersion(int version) {
		this.version = version;
	}
	
    public byte[] getFlag() {
		return flag;
	}
	
    public void setFlag(byte[] flag) {
		this.flag = flag;
	}

}
