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

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import nl.siwoc.mediainfo.FileProber;
import nl.siwoc.mediainfo.MediaInfo;

public class MKVUtils {

	private static final Logger LOGGER = Logger.getLogger(MKVUtils.class.getName());

	public static MediaInfo parse(String filename) throws Exception {
		LOGGER.info("Start parsing file: " + filename);
		return new MKVFile(filename);
	}
	
	public static void main (String[] args) throws Exception {
		try {
			new File("log").mkdir();
			
			FileHandler handler = new FileHandler("log/FileProber.log", 500000, 2, true);
			handler.setFormatter(new SimpleFormatter());
			Logger.getLogger("").addHandler(handler);
			FileProber.setLogLevel(Level.FINER);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//String filename = "O:/downloads/Wallace and Gromit - A Matter of Loaf and Death (2008)/Wallace and Gromit - A Matter of Loaf and Death (2008).mkv";
		String filename = "O:/downloads/Aladdin (1992)/Aladdin (1992).mkv";
		MediaInfo mediaInfo = MKVUtils.parse(filename);
		System.out.println(mediaInfo.getContainer());
		System.out.println(mediaInfo.getVideoCodec());
		System.out.println(mediaInfo.getFrameWidth());
		System.out.println(mediaInfo.getFrameHeight());
		System.out.println(mediaInfo.getAudioCodec());
		System.out.println(mediaInfo.getAudioChannels());
	}

}
