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
package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import nl.siwoc.mediainfo.utils.Logger;
import nl.siwoc.mediainfo.utils.ReadUtils;

public class Mp4aSampleEntry extends SampleEntry {
	
	private int channelCount;
	private int sampleSize;
	private long sampleRate;
	
	public int getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(int channelCount) {
		this.channelCount = channelCount;
	}

	public int getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}

	public long getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(long sampleRate) {
		this.sampleRate = sampleRate;
	}

	public Mp4aSampleEntry(Box parent, long size, byte[] data) throws Exception {
		setType("mp4a");
		setSize(size);
		setParent(parent);
		Logger.logInfo("Creating " + getType());
        TrakBox trak = (TrakBox)searchUp("trak");
        if (trak != null) {
        	trak.setCodecId(getType());
        }
		try (InputStream is = new ByteArrayInputStream(data)){
			// SampleEntry base
			// skip 6
			ReadUtils.readUInt32BE(is);
			ReadUtils.readInt16BE(is);
			setDataReferenceIndex(ReadUtils.readUInt16BE(is));
			// AudioSampleEntry
			// skip 8 (0)
			ReadUtils.readUInt32BE(is);
			ReadUtils.readUInt32BE(is);
			setChannelCount(ReadUtils.readUInt16BE(is));
			setSampleSize(ReadUtils.readUInt16BE(is));
			ReadUtils.readUInt16BE(is);
			setSampleRate(ReadUtils.readUInt32BE(is));
			if (trak != null) {
				trak.setChannelCount(getChannelCount());
			}
			Logger.logInfo(toString());
			
		} catch (Exception e) {
			throw e;
		}
	}

	public String toString() {
		return "Mp4aSampleEntry{ " +
	            "channelCount=" + getChannelCount() +
	            ", sampleSize=" + getSampleSize() +
	            ", sampleRate=" + getSampleRate() +
	            " }";
	}
}
