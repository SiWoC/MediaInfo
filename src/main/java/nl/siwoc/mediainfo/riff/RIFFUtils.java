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
package nl.siwoc.mediainfo.riff;

import java.io.FileInputStream;
import nl.siwoc.mediainfo.MediaInfo;
import nl.siwoc.mediainfo.riff.avi.AVIRIFF;
import nl.siwoc.mediainfo.utils.Logger;
import nl.siwoc.mediainfo.utils.ReadUtils;

public class RIFFUtils {

	public static MediaInfo parse(String filename) throws Exception {
		Logger.logInfo("Start parsing file: " + filename);
		try (FileInputStream fis = new FileInputStream(filename)) {
			String fourCC = ReadUtils.readFourCC(fis);
			Logger.logInfo("Found fourCC: [" + fourCC + "] should be [RIFF]");
			if (!"RIFF".equals(fourCC)) {
				throw new Exception("File is not RIFF");
			}
			int size = ReadUtils.readInt32LE(fis);
			String fileType = ReadUtils.readFourCC(fis);
			Logger.logInfo("Found fileType: [" + fileType + "]");
			if (!"AVI ".equals(fileType)) {
				throw new Exception("RIFF FileType: " + fileType + " is not supported");
			}
			return new AVIRIFF(fileType, size, fis);
		} catch (Exception e) {
			throw e;
		}
	}

	public static void main (String[] args) throws Exception {
		Logger.setLogLevel("TRACE");
		String filename = "O:\\Kinder films\\Monsters Versus Aliens - Cloning Around (2014)\\Monsters Versus Aliens - Cloning Around (2014) [ID imdb tt2782214].avi";
		//String filename = "O:/downloads/Shazam (2019)/Shazam (2019).avi";
		MediaInfo mediaInfo = RIFFUtils.parse(filename);
		System.out.println(mediaInfo.getContainer());
		System.out.println(mediaInfo.getVideoCodec());
		System.out.println(mediaInfo.getFrameWidth());
		System.out.println(mediaInfo.getFrameHeight());
		System.out.println(mediaInfo.getAudioCodec());
		System.out.println(mediaInfo.getAudioChannels());
	}
	
}
