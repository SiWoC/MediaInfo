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
package nl.siwoc.mediainfo.riff.avi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import nl.siwoc.mediainfo.utils.ReadUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaveFormat extends StreamFormat {
	
	protected static final Logger LOG = LoggerFactory.getLogger(WaveFormat.class);

	private short formatTag;
	private int channels; 
	private int samplesPerSecond; 
	private int averageBytesPerSecond; 
	private short blockAlign; 
	private short bitsPerSample; 
	private short extraInfoSize; 

	public short getFormatTag() {
		return formatTag;
	}

	public void setFormatTag(short formatTag) {
		this.formatTag = formatTag;
	}

	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		this.channels = channels;
	}

	public int getSamplesPerSecond() {
		return samplesPerSecond;
	}

	public void setSamplesPerSecond(int samplesPerSecond) {
		this.samplesPerSecond = samplesPerSecond;
	}

	public int getAverageBytesPerSecond() {
		return averageBytesPerSecond;
	}

	public void setAverageBytesPerSecond(int averageBytesPerSecond) {
		this.averageBytesPerSecond = averageBytesPerSecond;
	}

	public short getBlockAlign() {
		return blockAlign;
	}

	public void setBlockAlign(short blockAlign) {
		this.blockAlign = blockAlign;
	}

	public short getBitsPerSample() {
		return bitsPerSample;
	}

	public void setBitsPerSample(short bitsPerSample) {
		this.bitsPerSample = bitsPerSample;
	}

	public short getExtraInfoSize() {
		return extraInfoSize;
	}

	public void setExtraInfoSize(short extraInfoSize) {
		this.extraInfoSize = extraInfoSize;
	}
	
	public static WaveFormat getInstance(int size, byte[] data) throws Exception {
		WaveFormat wf = new WaveFormat(size, data);
		switch(wf.getFormatTag()) {
			case WaveFormatTag.WAVE_FORMAT_MPEG: {
				return new WaveFormatMpeg(wf);
			}
			case WaveFormatTag.WAVE_FORMAT_MPEGLAYER3: {
				return new WaveFormatMpegLayer3(wf);
			}
			case WaveFormatTag.WAVE_FORMAT_EXTENSIBLE: {
				return new WaveFormatExtensible(wf);
			}
			case WaveFormatTag.WAVE_FORMAT_DVM: {
				return new WaveFormatDVM(wf);
			}
			default: {
				LOG.error("Unsupported formatTag=" + Integer.toHexString(wf.getFormatTag() & 0xffff));
				return wf;
			}
		}
	}

	public WaveFormat() {}
	
	public WaveFormat(int size, byte[] data) throws Exception {
		try (InputStream is = new ByteArrayInputStream(data)){
			setFormatTag(ReadUtils.readInt16LE(is));
			setChannels(ReadUtils.readInt16LE(is)); 
			setSamplesPerSecond(ReadUtils.readInt32LE(is)); 
			setAverageBytesPerSecond(ReadUtils.readInt32LE(is)); 
			setBlockAlign(ReadUtils.readInt16LE(is)); 
			setBitsPerSample(ReadUtils.readInt16LE(is)); 
			try {
				setExtraInfoSize(ReadUtils.readInt16LE(is));
			} catch (Exception e) {
				// No extra info
			}
			LOG.info("WaveFormat" + System.lineSeparator() +
				"  formatTag=" + Integer.toHexString(getFormatTag() & 0xffff) + System.lineSeparator() +
				"  channels=" + getChannels() + System.lineSeparator() +
				"  samplesPerSecond=" + getSamplesPerSecond() + System.lineSeparator() +
				"  averageBytesPerSecond=" + getAverageBytesPerSecond() + System.lineSeparator() +
				"  blockAlign=" + getBlockAlign() + System.lineSeparator() +
				"  bitsPerSample=" + getBitsPerSample() + System.lineSeparator() +
				"  extraInfoSize=" + getExtraInfoSize());
		} catch (Exception e) {
			throw e;
		}

	}
	
	public String getHandler() {
		return "ac3";
	}

}
