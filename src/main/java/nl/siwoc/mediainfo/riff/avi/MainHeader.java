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
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.siwoc.mediainfo.riff.Chunk;
import nl.siwoc.mediainfo.riff.RIFFUtils;

public class MainHeader extends Chunk {

	private static final Logger LOGGER = Logger.getLogger(MainHeader.class.getName());
	
	private int microSecondsPerFrame;
	private int maxBytesPerSecond;
	private int paddingGranularity;
	private int flags;
	private int totalFrames;
	private int initialFrames;
	private int numberOfStreams;
	private int suggestedBufferSize;
	private int frameWidth;
	private int frameHeight;

	public int getMicroSecondsPerFrame() {
		return microSecondsPerFrame;
	}

	public void setMicroSecondsPerFrame(int microSecondsPerFrame) {
		this.microSecondsPerFrame = microSecondsPerFrame;
	}

	public int getMaxBytesPerSecond() {
		return maxBytesPerSecond;
	}

	public void setMaxBytesPerSecond(int maxBytesPerSecond) {
		this.maxBytesPerSecond = maxBytesPerSecond;
	}

	public int getPaddingGranularity() {
		return paddingGranularity;
	}

	public void setPaddingGranularity(int paddingGranularity) {
		this.paddingGranularity = paddingGranularity;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int getTotalFrames() {
		return totalFrames;
	}

	public void setTotalFrames(int totalFrames) {
		this.totalFrames = totalFrames;
	}

	public int getInitialFrames() {
		return initialFrames;
	}

	public void setInitialFrames(int initialFrames) {
		this.initialFrames = initialFrames;
	}

	public int getNumberOfStreams() {
		return numberOfStreams;
	}

	public void setNumberOfStreams(int numberOfStreams) {
		this.numberOfStreams = numberOfStreams;
	}

	public int getSuggestedBufferSize() {
		return suggestedBufferSize;
	}

	public void setSuggestedBufferSize(int suggestedBufferSize) {
		this.suggestedBufferSize = suggestedBufferSize;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	public MainHeader(int size, byte[] data) throws Exception {
		setId("avih");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(data)){
			setMicroSecondsPerFrame(RIFFUtils.readIntLE(is));
			setMaxBytesPerSecond(RIFFUtils.readIntLE(is));
			setPaddingGranularity(RIFFUtils.readIntLE(is)); //in newer avi formats, this is dwPaddingGranularity?
			setFlags(RIFFUtils.readIntLE(is));
			setTotalFrames(RIFFUtils.readIntLE(is));
			setInitialFrames(RIFFUtils.readIntLE(is));
			setNumberOfStreams(RIFFUtils.readIntLE(is));
			setSuggestedBufferSize(RIFFUtils.readIntLE(is));
			setFrameWidth(RIFFUtils.readIntLE(is));
			setFrameHeight(RIFFUtils.readIntLE(is));
			// dwReserved[4] follows, ignored

			LOGGER.log(Level.FINE,"AVI HEADER (avih): " + System.lineSeparator() +
				"   microSecondsPerFrame=" + getMicroSecondsPerFrame() + System.lineSeparator() +
				"   maxBytesPerSecond=" + getMaxBytesPerSecond() + System.lineSeparator() +
				"   paddingGranularity=" + getPaddingGranularity() + System.lineSeparator() +
				"   flags=" + getFlags() + System.lineSeparator() +
				"   totalFrames=" + getTotalFrames() + System.lineSeparator() +
				"   initialFrames=" + getInitialFrames() + System.lineSeparator() +
				"   numberOfStreams=" + getNumberOfStreams() + System.lineSeparator() +
				"   suggestedBufferSize=" + getSuggestedBufferSize() + System.lineSeparator() +
				"   frameWidth=" + getFrameWidth() + System.lineSeparator() +
				"   frameHeight=" + getFrameHeight() + System.lineSeparator());
		} catch (Exception e) {
			throw e;
		}

	}

}
