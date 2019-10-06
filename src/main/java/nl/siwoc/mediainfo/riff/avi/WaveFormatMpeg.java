package nl.siwoc.mediainfo.riff.avi;

public class WaveFormatMpeg extends WaveFormat {

	private WaveFormat waveFormat;
	
	public WaveFormat getWaveFormat() {
		return waveFormat;
	}

	public void setWaveFormat(WaveFormat waveFormat) {
		this.waveFormat = waveFormat;
	}

	public WaveFormatMpeg(WaveFormat wf) {
		setWaveFormat(wf);
	}

	public String getHandler() {
		return "mp3";
	}

	public short getChannels() {
		return getWaveFormat().getChannels();
	}

}
