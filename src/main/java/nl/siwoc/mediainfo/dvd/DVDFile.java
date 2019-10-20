package nl.siwoc.mediainfo.dvd;

import java.io.FileInputStream;
import java.util.logging.Logger;

import nl.siwoc.mediainfo.MediaInfo;

public class DVDFile implements MediaInfo {

	private static final Logger LOGGER = Logger.getLogger(DVDFile.class.getName());
	
	private String videoCodec;
	private String standard;
	private String aspect;
	private int frameWidth;
	private int frameHeight;
	private short numberOfAudioStreams;
	private String audioCodec;
	private short audioChannels;

	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getAspect() {
		return aspect;
	}

	public void setAspect(String aspect) {
		this.aspect = aspect;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	public short getNumberOfAudioStreams() {
		return numberOfAudioStreams;
	}

	public void setNumberOfAudioStreams(short numberOfAudioStreams) {
		this.numberOfAudioStreams = numberOfAudioStreams;
	}

	public void setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
	}

	public void setAudioChannels(short audioChannels) {
		this.audioChannels = audioChannels;
	}

	public DVDFile (String filename) throws Exception {
		LOGGER.info("Start parsing file: " + filename);
		try (FileInputStream fis = new FileInputStream(filename)) {
			byte[] type = new byte[12];
			fis.read(type);
			if (!new String(type, "ASCII").matches("DVDVIDEO-VMG|DVDVIDEO-VTS")) {
				throw new Exception("Invalid DVDFile, could not find DVDVIDEO-VMG or DVDVIDEO-VTS");
			}
			// skipping to offset 0x0200 videoattributes
			fis.skip(500);
			// videoattributes: Coding mode, Standard, Aspect, Automatic Pan/Scan, Automatic Letterbox
			int attributes = fis.read();
			switch (DVDUtils.getTwoBits(attributes, 6)) {
				case 0: 
					setVideoCodec("mpeg1");
					break;
				case 1:
					setVideoCodec("mpeg2");
					break;
			}
			switch (DVDUtils.getTwoBits(attributes, 4)) {
				case 0: 
					setStandard("NTSC");
					break;
				case 1:
					setStandard("PAL");
					break;
			}
			switch (DVDUtils.getTwoBits(attributes, 2)) {
				case 0: 
					setAspect("4:3");
					break;
				case 1: case 2:
					setAspect("reserved");
					break;
				case 3:
					setAspect("16:9");
					break;
			}
			// videoattributes: CC for line 21, Resolution, Letterboxed, camera/film
			attributes = fis.read();
			switch (DVDUtils.getThreeBits(attributes, 3)) {
			case 0: 
				setFrameWidth(720);
				if ("NTSC".equals(getStandard())) {
					setFrameHeight(480);
				} else {
					setFrameHeight(576);
				}
				break;
			case 1:
				setFrameWidth(704);
				if ("NTSC".equals(getStandard())) {
					setFrameHeight(480);
				} else {
					setFrameHeight(576);
				}
				break;
			case 2:
				setFrameWidth(352);
				if ("NTSC".equals(getStandard())) {
					setFrameHeight(480);
				} else {
					setFrameHeight(576);
				}
				break;
			case 3:
				setFrameWidth(352);
				if ("NTSC".equals(getStandard())) {
					setFrameHeight(240);
				} else {
					setFrameHeight(288);
				}
				break;
			}
			// number of audio streams
			setNumberOfAudioStreams(DVDUtils.readShortBE(fis));
			if (getNumberOfAudioStreams() > 0) {
				// audioattributes: Coding mode,, Multichannel extension present, Language type, Application mode
				attributes = fis.read();
				switch (DVDUtils.getThreeBits(attributes, 5)) {
					case 0: 
						setAudioCodec("ac3");
						break;
					case 1:
						setAudioCodec("nn3");
						break;
					case 2:
						setAudioCodec("mpeg1");
						break;
					case 3:
						setAudioCodec("mpeg2");
						break;
					case 4:
						setAudioCodec("lpcm");
						break;
					case 5:
						setAudioCodec("nn5");
						break;
					case 6:
						setAudioCodec("dts");
						break;
					case 7:
						setAudioCodec("nn7");
						break;
				}
				// audioattributes: Quantization/DRC, Sample rate, AudioChannels
				attributes = fis.read();
				setAudioChannels((short) (DVDUtils.getThreeBits(attributes, 0) + 1));
			}
			LOGGER.info(toString());
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public String getContainer() {
		return "dvd";
	}

	@Override
	public String getVideoCodec() {
		return videoCodec;
	}

	@Override
	public int getFrameWidth() {
		return frameWidth;
	}

	@Override
	public int getFrameHeight() {
		return frameHeight;
	}

	@Override
	public String getAudioCodec() {
		return audioCodec;
	}

	@Override
	public short getAudioChannels() {
		return audioChannels;
	}

	public String toString() {
		return "DVDFile {" +
				" videoCodec: " + getVideoCodec() +
				", standard: " + getStandard() +
				", aspect: " + getAspect() +
				", frameWidth: " + getFrameWidth() +
				", frameHeight: " + getFrameHeight() +
				", audioCodec: " + getAudioCodec() +
				", audioChannels: " + getAudioChannels() +
				" }";
	}
}
