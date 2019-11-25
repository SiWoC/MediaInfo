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
package nl.siwoc.mediainfo.mkv;

import nl.siwoc.mediainfo.MediaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MKVUtils {
	
	protected static final Logger LOG = LoggerFactory.getLogger(MKVUtils.class);

	public static MediaInfo parse(String filename) throws Exception {
		LOG.info("Start parsing file: " + filename);
		return new MKVFile(filename);
	}
	
	public static void main (String[] args) throws Exception {
		//Logger.setLogLevel("TRACE");

		//String filename = "O:/downloads/Wallace and Gromit - A Matter of Loaf and Death (2008)/Wallace and Gromit - A Matter of Loaf and Death (2008).mkv";
		//String filename = "O:/Films/Pacific Rim Uprising (2018)/Pacific Rim Uprising (2018).mkv";
		String filename = "O:/Films/Gräns (2018)/Grans (2018).mkv";
		MediaInfo mediaInfo = MKVUtils.parse(filename);
		System.out.println(mediaInfo.getContainer());
		System.out.println(mediaInfo.getVideoCodec());
		System.out.println(mediaInfo.getFrameWidth());
		System.out.println(mediaInfo.getFrameHeight());
		System.out.println(mediaInfo.getAudioCodec());
		System.out.println(mediaInfo.getAudioChannels());
	}

}
