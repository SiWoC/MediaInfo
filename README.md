# MediaInfo
Extracting MediaInfo, like container, videocodec, width, height, audiocodec and number of audiochannels with pure Java

```
FileProber fp = new FileProber();
MediaInfo mediaInfo = fp.getMediaInfo(filename);

public interface MediaInfo {
	
	public String getContainer();
	
	public String getVideoCodec();
	
	public int getFrameWidth();
	
	public int getFrameHeight();
	
	public String getAudioCodec();
	
	public short getAudioChannels();
	
}
```
