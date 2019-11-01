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

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import nl.siwoc.mediainfo.FileProber;
import nl.siwoc.mediainfo.MediaInfo;
import nl.siwoc.mediainfo.dvd.DVDFile;

public class ISOUtils {

	private static final Logger LOGGER = Logger.getLogger(ISOUtils.class.getName());

	public static MediaInfo parse(String filename) throws Exception {
		LOGGER.info("Start parsing file: " + filename);
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
		DVDFile dvdFile = null;
		for (int i = 1 ; i < 100 ; i++) {
			String vtsFilename = "VIDEO_TS" + System.getProperty("file.separator") + "VTS_" + String.format("%02d", i) + "_0.IFO";
			LOGGER.info("Searching vts-file: " + vtsFilename);
			ISO9660DiskImageFS vtsFile = iso.getFile(vtsFilename);
			if (vtsFile == null) {
				throw new Exception("No DVD ISO file [VTS_xx_0.IFO] found.");
			}
			byte[] vtsFileData = vtsFile.getData();
			dvdFile = DVDFile.parseFromByteArray(vtsFileData);
			dvdFile.setContainer("dvdiso");
			if (dvdFile.getNumberOfAudioStreams() > 0) {
				return dvdFile;
			}
			LOGGER.info("vts-file: " + vtsFilename + " has no AudioStreams searching next IFO");
		}
		return dvdFile;
		//throw new Exception("No DVD ISO file [VTS_xx_0.IFO] found.");
	}

	public static void main (String args[]) throws Exception {
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

		String filename = "O:/Kinder films/Early Man (2018)/Early Man (2018).iso";
		//String filename = "O:/downloads/Aladdin (1992)/Aladdin (1992).mkv";
		MediaInfo iso = ISOUtils.parse(filename);
		System.out.println(iso.getContainer());
		System.out.println(iso.getVideoCodec());

	}	

	public static int readIntLE(byte[] data, int offset) throws Exception {
		if (data.length < offset + 4) throw new Exception("data too short [" + data.length + "] for offset + 4 [" + offset + "], could not readIntLE");
		byte[] bytes = Arrays.copyOfRange(data, offset, offset + 4);
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	public static short getUnsignedValue(byte b) {
		return (short) (b & 0xFF);
	}

	public static int getSingleBit(int b, int position)
	{
	   return (b >> position) & 1;
	}

	
}
