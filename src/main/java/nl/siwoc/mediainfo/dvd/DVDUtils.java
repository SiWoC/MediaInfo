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
package nl.siwoc.mediainfo.dvd;

import java.io.File;
import java.io.FilenameFilter;

import nl.siwoc.mediainfo.MediaInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DVDUtils {

	protected static final Logger LOG = LoggerFactory.getLogger(DVDUtils.class);
	
	public static MediaInfo parse(String filename) throws Exception {
		LOG.info("Start parsing file: " + filename);
		File file = new File(filename);
		if (file.getName().matches("VTS_\\d\\d_0.IFO")) {
			LOG.debug("Found DVD-file at: " + file.getAbsolutePath());
			return DVDFile.parseFromFile(file.getAbsolutePath());
		} else if (file.isDirectory() && file.getName().equals("VIDEO_TS")) {
			return parseVTS(file);
		} else if (file.isDirectory()) {
			file = new File(file, "VIDEO_TS");
			if (file.isDirectory()) {
				return parseVTS(file);
			}
		}
		throw new Exception("No DVD file [VTS_xx_0.IFO] found.");
	}

	private static MediaInfo parseVTS(File videoTsFolder) throws Exception {
		DVDFile result = null;
		File[] vtsFiles = videoTsFolder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.matches("VTS_\\d\\d_0.IFO");
			}
		});

		int numberOfAudioChannels = 0;
		DVDFile dvdFile = null;
		for (File file : vtsFiles) {
			if (file.isFile()) {
				dvdFile = DVDFile.parseFromFile(file.getAbsolutePath());
				if (dvdFile.getNumberOfAudioStreams() > 0 && dvdFile.getAudioChannels() > numberOfAudioChannels) {
					numberOfAudioChannels = dvdFile.getAudioChannels();
					result = dvdFile;
				}
			}
		}
		if (result == null) {
			return dvdFile;
		} else {
			return result;
		}
	}

	public static void main (String args[]) throws Exception {
		//Logger.setLogLevel("TRACE");
		//String filename = "O:\\Kinder films\\Flight of the Navigator (1986)\\VIDEO_TS\\VTS_01_0.IFO";
		String filename = "O:/Kinder films/G-Force (2009)/VIDEO_TS/VTS_07_0.IFO";
		MediaInfo dvd = DVDUtils.parse(filename);
		System.out.println(dvd.getContainer());
		System.out.println(dvd.getVideoCodec());
	}

}
