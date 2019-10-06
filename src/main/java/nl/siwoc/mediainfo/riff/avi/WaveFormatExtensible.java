package nl.siwoc.mediainfo.riff.avi;

public class WaveFormatExtensible extends WaveFormat {

	private WaveFormat waveFormat;
	
	public WaveFormat getWaveFormat() {
		return waveFormat;
	}

	public void setWaveFormat(WaveFormat waveFormat) {
		this.waveFormat = waveFormat;
	}

	public WaveFormatExtensible(WaveFormat wf) {
		setWaveFormat(wf);
	}
	
	public short getChannels() {
		return getWaveFormat().getChannels();
	}

}
