/*******************************************************************************
 * Copyright (c) 2019 Niek Knijnenburg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package nl.siwoc.mediainfo.dvd;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import nl.siwoc.mediainfo.MediaInfo;
import nl.siwoc.mediainfo.utils.Logger;
import nl.siwoc.mediainfo.utils.ReadUtils;

public class DVDFile implements MediaInfo {

	private String container;
	private String videoCodec;
	private String standard;
	private String aspect;
	private int frameWidth;
	private int frameHeight;
	private short numberOfAudioStreams;
	private String audioCodec;
	private int audioChannels;

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

	public void setAudioChannels(int audioChannels) {
		this.audioChannels = audioChannels;
	}

	public static DVDFile parseFromFile(String filename) throws Exception {
		Logger.logInfo("Start parsing file: " + filename);
		try (FileInputStream fis = new FileInputStream(filename)) {
			return parseFromStream(fis);
		} catch (Exception e) {
			throw e;
		}
	}

	public static DVDFile parseFromStream(InputStream is) throws Exception {
		DVDFile dvd = new DVDFile();
		try {
			byte[] type = new byte[12];
			is.read(type);
			if (!new String(type, "ASCII").matches("DVDVIDEO-VMG|DVDVIDEO-VTS")) {
				throw new Exception("Invalid DVDFile, could not find DVDVIDEO-VMG or DVDVIDEO-VTS");
			}
			dvd.setContainer("dvd");
			// skipping to offset 0x0200 videoattributes
			is.skip(500);
			// videoattributes: Coding mode, Standard, Aspect, Automatic Pan/Scan, Automatic Letterbox
			int attributes = is.read();
			switch (ReadUtils.getTwoBits(attributes, 6)) {
				case 0: 
					dvd.setVideoCodec("mpeg1");
					break;
				case 1:
					dvd.setVideoCodec("mpeg2");
					break;
			}
			switch (ReadUtils.getTwoBits(attributes, 4)) {
				case 0: 
					dvd.setStandard("NTSC");
					break;
				case 1:
					dvd.setStandard("PAL");
					break;
			}
			switch (ReadUtils.getTwoBits(attributes, 2)) {
				case 0: 
					dvd.setAspect("4:3");
					break;
				case 1: case 2:
					dvd.setAspect("reserved");
					break;
				case 3:
					dvd.setAspect("16:9");
					break;
			}
			// videoattributes: CC for line 21, Resolution, Letterboxed, camera/film
			attributes = is.read();
			switch (ReadUtils.getThreeBits(attributes, 3)) {
			case 0: 
				dvd.setFrameWidth(720);
				if ("NTSC".equals(dvd.getStandard())) {
					dvd.setFrameHeight(480);
				} else {
					dvd.setFrameHeight(576);
				}
				break;
			case 1:
				dvd.setFrameWidth(704);
				if ("NTSC".equals(dvd.getStandard())) {
					dvd.setFrameHeight(480);
				} else {
					dvd.setFrameHeight(576);
				}
				break;
			case 2:
				dvd.setFrameWidth(352);
				if ("NTSC".equals(dvd.getStandard())) {
					dvd.setFrameHeight(480);
				} else {
					dvd.setFrameHeight(576);
				}
				break;
			case 3:
				dvd.setFrameWidth(352);
				if ("NTSC".equals(dvd.getStandard())) {
					dvd.setFrameHeight(240);
				} else {
					dvd.setFrameHeight(288);
				}
				break;
			}
			// number of audio streams
			dvd.setNumberOfAudioStreams(ReadUtils.readInt16BE(is));
			if (dvd.getNumberOfAudioStreams() > 0) {
				// audioattributes: Coding mode,, Multichannel extension present, Language type, Application mode
				attributes = is.read();
				switch (ReadUtils.getThreeBits(attributes, 5)) {
					case 0: 
						dvd.setAudioCodec("ac3");
						break;
					case 1:
						dvd.setAudioCodec("nn3");
						break;
					case 2:
						dvd.setAudioCodec("mpeg1");
						break;
					case 3:
						dvd.setAudioCodec("mpeg2");
						break;
					case 4:
						dvd.setAudioCodec("lpcm");
						break;
					case 5:
						dvd.setAudioCodec("nn5");
						break;
					case 6:
						dvd.setAudioCodec("dts");
						break;
					case 7:
						dvd.setAudioCodec("nn7");
						break;
				}
				// audioattributes: Quantization/DRC, Sample rate, AudioChannels
				attributes = is.read();
				dvd.setAudioChannels((short) (ReadUtils.getThreeBits(attributes, 0) + 1));
			}
			Logger.logInfo(dvd.toString());
			return dvd;
		} catch (Exception e) {
			throw e;
		}
	}

	public static DVDFile parseFromByteArray(byte[] vtsFileData) throws Exception {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(vtsFileData)) {
			return parseFromStream(bis);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
		
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
	public int getAudioChannels() {
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
