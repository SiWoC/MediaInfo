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

import nl.siwoc.mediainfo.riff.Chunk;
import nl.siwoc.mediainfo.utils.ReadUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamHeader extends Chunk{
	
	protected static final Logger LOG = LoggerFactory.getLogger(StreamHeader.class);

	private String type;
	private String handler;
	private int flags;
	private int priorityLanguage;
	private int initialFrames;
	private int scale;
	private int rate;
	private int start;
	private int length;
	private int suggestedBufferSize;
	private int quality;
	private int sampleSize;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int Flags) {
		this.flags = Flags;
	}

	public int getPriorityLanguage() {
		return priorityLanguage;
	}

	public void setPriorityLanguage(int priorityLanguage) {
		this.priorityLanguage = priorityLanguage;
	}

	public int getInitialFrames() {
		return initialFrames;
	}

	public void setInitialFrames(int InitialFrames) {
		this.initialFrames = InitialFrames;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int Scale) {
		this.scale = Scale;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int Rate) {
		this.rate = Rate;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int Start) {
		this.start = Start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int Length) {
		this.length = Length;
	}

	public int getSuggestedBufferSize() {
		return suggestedBufferSize;
	}

	public void setSuggestedBufferSize(int SuggestedBufferSize) {
		this.suggestedBufferSize = SuggestedBufferSize;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int Quality) {
		this.quality = Quality;
	}

	public int getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(int SampleSize) {
		this.sampleSize = SampleSize;
	}

	public StreamHeader(int size, byte[] data) throws Exception{
		setId("strh");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(data)){
			setType(ReadUtils.readFourCC(is));
			setHandler(ReadUtils.readFourCC(is));
			setFlags(ReadUtils.readInt32LE(is));
			setPriorityLanguage(ReadUtils.readInt32LE(is));
			setInitialFrames(ReadUtils.readInt32LE(is));
			setScale(ReadUtils.readInt32LE(is));
			setRate(ReadUtils.readInt32LE(is));
			setStart(ReadUtils.readInt32LE(is));
			setLength(ReadUtils.readInt32LE(is));
			setSuggestedBufferSize(ReadUtils.readInt32LE(is));
			setQuality(ReadUtils.readInt32LE(is));
			setSampleSize(ReadUtils.readInt32LE(is));
			//rcFrame rectangle follows, ignored

			LOG.info("Stream Header (strh):" + System.lineSeparator() +
				"   type=[" + type + "]" + System.lineSeparator() +
				"   handler=[" + handler + "]" + System.lineSeparator() +
				"   flags=" + flags + System.lineSeparator() +
				"   wPriority,wLanguage=" + priorityLanguage + System.lineSeparator() +
				"   initialFrames=" + initialFrames + System.lineSeparator() +
				"   scale=" + scale + System.lineSeparator() +
				"   rate=" + rate + System.lineSeparator() +
				"   start=" + start + System.lineSeparator() +
				"   length=" + length + System.lineSeparator() +
				"   suggestedBufferSize=" + suggestedBufferSize + System.lineSeparator() +
				"   quality=" + quality + System.lineSeparator() +
				"   sampleSize=" + sampleSize);
		} catch (Exception e) {
			throw e;
		}
	}

}
