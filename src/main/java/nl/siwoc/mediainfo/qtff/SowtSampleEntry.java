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

public class SowtSampleEntry extends SampleEntry {
	
	private short channelCount;
	private short sampleSize;
	private int sampleRate;
	
	public short getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(short channelCount) {
		this.channelCount = channelCount;
	}

	public short getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(short sampleSize) {
		this.sampleSize = sampleSize;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	public SowtSampleEntry(Box parent, int size, byte[] data) throws Exception {
		setType("sowt");
		setSize(size);
		setParent(parent);
		LOGGER.info("Creating " + getType());
        TrakBox trak = (TrakBox)searchUp("trak");
        if (trak != null) {
        	trak.setCodecId(getType());
        }
		try (InputStream is = new ByteArrayInputStream(data)){
			// SampleEntry base
			// skip 6
			QTFFUtils.readIntBE(is);
			QTFFUtils.readShortBE(is);
			setDataReferenceIndex(QTFFUtils.readShortBE(is));
			// VisualSampleEntry
			// skip 8 (0)
			QTFFUtils.readIntBE(is);
			QTFFUtils.readIntBE(is);
			setChannelCount(QTFFUtils.readShortBE(is));
			setSampleSize(QTFFUtils.readShortBE(is));
			QTFFUtils.readShortBE(is);
			setSampleRate(QTFFUtils.readIntBE(is));
			if (trak != null) {
				trak.setChannelCount(getChannelCount());
			}
			LOGGER.info(toString());
			
		} catch (Exception e) {
			throw e;
		}
	}

	public String toString() {
		return "SowtSampleEntry{ " +
	            "channelCount=" + getChannelCount() +
	            ", sampleSize=" + getSampleSize() +
	            ", sampleRate=" + getSampleRate() +
	            " }";
	}
}
