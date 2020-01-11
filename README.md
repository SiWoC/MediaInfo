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

For now this supports mp4, avi, mkv, qt, mov, dvd-iso and dvd-folders.  
For dvd-folders you can pass the parent-folder, the VIDEO\_TS-folder or a VTS\_xx\_0.IFO-file

It relies on the org.jebml (which I forked to fix a NullPointer) for MKV files

It is not as versatile as https://github.com/MediaArea/MediaInfo but it's enough for me and it's pure Java, no wrappers, no dll's.
