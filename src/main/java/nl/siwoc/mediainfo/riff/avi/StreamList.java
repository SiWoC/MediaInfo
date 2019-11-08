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

import nl.siwoc.mediainfo.riff.ListChunk;
import nl.siwoc.mediainfo.utils.ReadUtils;

public class StreamList extends ListChunk {

	private static final Logger LOGGER = Logger.getLogger(StreamList.class.getName());

	private StreamHeader strh;
	private StreamFormat strf;

	public StreamHeader getStrh() {
		return strh;
	}

	public void setStrh(StreamHeader strh) {
		this.strh = strh;
	}

	public StreamFormat getStrf() {
		return strf;
	}

	public void setStrf(StreamFormat strf) {
		this.strf = strf;
	}

	public StreamList(int size, byte[] data) throws Exception {
		setListType("strl");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(data)) {
			String fourCC;
			int childSize;
			byte[] childData;
			// expect strh
			fourCC = ReadUtils.readFourCC(is);
			if (!"strh".equals(fourCC)) {
				throw new Exception("Invalid AVI StreamList, unable to find strh, found: " + fourCC);
			}
			childSize = ReadUtils.readInt32LE(is);
			childData = new byte[childSize];
			is.read(childData);
			setStrh(new StreamHeader(childSize, childData));
			// strf
			fourCC = ReadUtils.readFourCC(is);
			if (!"strf".equals(fourCC)) {
				throw new Exception("Invalid AVI StreamList, unable to find strf, found: " + fourCC);
			}
			childSize = ReadUtils.readInt32LE(is);
			LOGGER.log(Level.FINE,"strfSize: " + childSize);
			childData = new byte[childSize];
			is.read(childData);
			if ("vids".equals(getStrh().getType())) {
				setStrf(new BitmapInfo(childSize, childData));
			} else if ("auds".equals(getStrh().getType())) {
				setStrf(WaveFormat.getInstance(childSize, childData));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public String getHandler() {
		if ("vids".equals(getStrh().getType())) {
			return getStrh().getHandler();
		} else if ("auds".equals(getStrh().getType())) {
			return getStrf().getHandler();
		} else {
			return "NNSL";
		}
	}

	public int getChannels() {
		if ("auds".equals(getStrh().getType())) {
			return getStrf().getChannels();
		} else {
			return 0;
		}
	}

}
