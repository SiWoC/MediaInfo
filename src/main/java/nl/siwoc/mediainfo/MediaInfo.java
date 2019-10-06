package nl.siwoc.mediainfo;

public interface MediaInfo {
	
	public String getContainer();
	
	public String getVideoCodec();
	
	public int getFrameWidth();
	
	public int getFrameHeight();
	
	public String getAudioCodec();
	
	public short getAudioChannels();
	
}
