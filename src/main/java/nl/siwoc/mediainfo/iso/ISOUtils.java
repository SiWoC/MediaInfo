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
package nl.siwoc.mediainfo.iso;

import nl.siwoc.mediainfo.MediaInfo;
import nl.siwoc.mediainfo.dvd.DVDFile;
import nl.siwoc.mediainfo.utils.Logger;

public class ISOUtils {

	public static MediaInfo parse(String filename) throws Exception {
		Logger.logInfo("Start parsing file: " + filename);
		ISO9660DiskImage f = null;
		try {
			f = new ISO9660DiskImage(filename);
			if (f.existsFile("VIDEO_TS")) {
				return createDVD(f);
			}
			throw new Exception("Could not find VIDEO_TS-folder, unsupported disk image");
		} catch (Exception e) {
			throw e;
		} finally {
			if (f != null) {
				f.close();
			}
		}
	}
	
	public static MediaInfo createDVD(ISO9660DiskImage iso) throws Exception {
		DVDFile result = null;
		int numberOfAudioChannels = 0;
		for (int i = 1 ; i < 100 ; i++) {
			String vtsFilename = "VIDEO_TS" + System.getProperty("file.separator") + "VTS_" + String.format("%02d", i) + "_0.IFO";
			Logger.logInfo("Searching vts-file: " + vtsFilename);
			ISO9660DiskImageFS vtsFile = iso.getFile(vtsFilename);
			if (vtsFile == null) {
				if (result != null) {
					return result;
				} else {
					throw new Exception("No DVD ISO file [VTS_xx_0.IFO] found.");
				}
			}
			try {
				byte[] vtsFileData = vtsFile.getData();
				DVDFile dvdFile = DVDFile.parseFromByteArray(vtsFileData);
				dvdFile.setContainer("dvdiso");
				if (dvdFile.getNumberOfAudioStreams() > 0 && dvdFile.getAudioChannels() > numberOfAudioChannels) {
					numberOfAudioChannels = dvdFile.getAudioChannels();
					result = dvdFile;
				}
				Logger.logInfo("vts-file: " + vtsFilename + " has [" + numberOfAudioChannels + "] AudioChannels searching next IFO");
			} catch (Exception e) {
				Logger.logInfo("Unable to parse vts-file: " + vtsFilename + " skipping.");
			}
		}
		return result;
		//throw new Exception("No DVD ISO file [VTS_xx_0.IFO] found.");
	}

	public static void main (String args[]) throws Exception {
		Logger.setLogLevel("TRACE");
		//String filename = "O:/Kinder films/Early Man (2018)/Early Man (2018).iso";
		String filename = "O:/Kinder films/Jasper & Julia en de Dappere Ridders/Jasper & Julia en de Dappere Ridders.iso";
		//String filename = "O:/downloads/Hotel Transylvania 3 Summer Vacation (2018)/Hotel Transylvania 3 Summer Vacation (2018).iso";
		MediaInfo iso = ISOUtils.parse(filename);
		System.out.println(iso.getContainer());
		System.out.println(iso.getVideoCodec());

	}

	
}
