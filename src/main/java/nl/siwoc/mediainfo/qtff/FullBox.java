package nl.siwoc.mediainfo.qtff;

public class FullBox extends Box {
    private int version;
    private int flag;
	
    public int getVersion() {
		return version;
	}
	
    public void setVersion(int version) {
		this.version = version;
	}
	
    public int getFlag() {
		return flag;
	}
	
    public void setFlag(int flag) {
		this.flag = flag;
	}

}
