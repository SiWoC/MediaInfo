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

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.siwoc.mediainfo.MediaInfo;
import nl.siwoc.mediainfo.riff.RIFFChunk;
import nl.siwoc.mediainfo.utils.ReadUtils;

public class AVIRIFF extends RIFFChunk implements MediaInfo{
	
	protected static final Logger LOG = LoggerFactory.getLogger(AVIRIFF.class);

	private HeaderList hdrl;

	public HeaderList getHdrl() {
		return hdrl;
	}

	public void setHdrl(HeaderList headerList) {
		this.hdrl = headerList;
	}

	public AVIRIFF(String fileType, int size, InputStream is) throws Exception {
		setId("RIFF");
		setSize(size);
		setFileType(fileType);
		String fourCC;
		fourCC = ReadUtils.readFourCC(is);
		if (!"LIST".equals(fourCC)) {
			throw new Exception("Invalid AVI RIFF, unable to find headerlist LIST, found: " + fourCC);
		}
		int hdrlSize = ReadUtils.readInt32LE(is);
		LOG.debug("hdrlSize: " + hdrlSize);
		fourCC = ReadUtils.readFourCC(is);
		if (!"hdrl".equals(fourCC)) {
			throw new Exception("Invalid AVI RIFF, unable to find headerlist hdrl, found: " + fourCC);
		}
		byte[] hdrlBytes = new byte[hdrlSize - 4];
		is.read(hdrlBytes);
		setHdrl(new HeaderList(hdrlSize, hdrlBytes));
		/* at the moment don't care about the rest
		fourCC = RIFFUtils.readFourCC(is);
		System.out.println("fourCC: " + fourCC);
		int LISTSize = RIFFUtils.readIntLE(is);
		System.out.println("LISTSize: " + LISTSize);
		String listType = RIFFUtils.readFourCC(is);
		System.out.println("listType: " + listType);
		byte[] bytes = new byte[1024];
		is.read(bytes);
		FileOutputStream fos = new FileOutputStream("c:/temp/avi.txt");
		fos.write(bytes);
		fos.close();
		System.out.println(new String(bytes, "ASCII"));
		*/
	}

	@Override
	public String getContainer() {
		return "avi";
	}

	@Override
	public String getVideoCodec() {
		if (getHdrl().getVideoStreams().size() > 0) {
			return getHdrl().getVideoStreams().get(0).getHandler().toLowerCase();
		} else {
			return null;
		}
	}

	@Override
	public int getFrameWidth() {
		return getHdrl().getAvih().getFrameWidth();
	}

	@Override
	public int getFrameHeight() {
		return getHdrl().getAvih().getFrameHeight();
	}

	@Override
	public String getAudioCodec() {
		if (getHdrl().getAudioStreams().size() > 0)
			return getHdrl().getAudioStreams().get(0).getHandler().toLowerCase();
		else {
			return null;
		}
	}

	@Override
	public int getAudioChannels() {
		if (getHdrl().getAudioStreams().size() > 0)
			return getHdrl().getAudioStreams().get(0).getChannels();
		else {
			return 0;
		}
	}

}
