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
import java.util.ArrayList;

import nl.siwoc.mediainfo.riff.ListChunk;
import nl.siwoc.mediainfo.utils.Logger;
import nl.siwoc.mediainfo.utils.ReadUtils;

public class HeaderList extends ListChunk {

	private MainHeader avih;
	private StreamList strl;
	private ArrayList<StreamList> videoStreams = new ArrayList<StreamList>();
	private ArrayList<StreamList> audioStreams = new ArrayList<StreamList>();

	public MainHeader getAvih() {
		return avih;
	}

	public void setAvih(MainHeader avih) {
		this.avih = avih;
	}

	public StreamList getStrl() {
		return strl;
	}

	public void setStrl(StreamList strl) {
		this.strl = strl;
	}

	public ArrayList<StreamList> getVideoStreams() {
		return videoStreams;
	}

	public void setVideoStreams(ArrayList<StreamList> videoStreams) {
		this.videoStreams = videoStreams;
	}

	public ArrayList<StreamList> getAudioStreams() {
		return audioStreams;
	}

	public void setAudioStreams(ArrayList<StreamList> audioStreams) {
		this.audioStreams = audioStreams;
	}

	public HeaderList(int size, byte[] data) throws Exception {
		setListType("hdrl");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(data)){
			String fourCC;
			// expect avih
			fourCC = ReadUtils.readFourCC(is);
			if (!"avih".equals(fourCC)) {
				throw new Exception("Invalid AVI HeaderList, should start with avih, found: " + fourCC);
			}
			// read and discard size = 56
			int avihSize = ReadUtils.readInt32LE(is);
			byte[] avihBytes = new byte[avihSize];
			is.read(avihBytes);
			setAvih(new MainHeader(avihSize, avihBytes));
			for (int i = 0 ; i < getAvih().getNumberOfStreams() ; i++) {
				// expect LIST
				fourCC = ReadUtils.readFourCC(is);
				if (!"LIST".equals(fourCC)) {
					throw new Exception("Invalid AVI HeaderList, unable to find header/streamlist LIST[" + i + "], found: " + fourCC);
				}
				int strlSize = ReadUtils.readInt32LE(is);
				Logger.logDebug("strlSize: " + strlSize);
				// expect strl
				fourCC = ReadUtils.readFourCC(is);
				if (!"strl".equals(fourCC)) {
					throw new Exception("Invalid AVI HeaderList, unable to find header/streamlist strl[\" + i + \"], found: " + fourCC);
				}
				byte[] strlBytes = new byte[strlSize - 4];
				is.read(strlBytes);
				StreamList strl = new StreamList(strlSize, strlBytes);
				String streamType = strl.getStrh().getType();
				if ("vids".equals(streamType)) {
					videoStreams.add(strl);
				} else if ("auds".equals(streamType)) {
					audioStreams.add(strl);
				} else {
					
				}
			}
		} catch (Exception e) {
			throw e;
		}

	}

}
