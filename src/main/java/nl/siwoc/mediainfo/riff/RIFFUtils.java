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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import nl.siwoc.mediainfo.FileProber;
import nl.siwoc.mediainfo.MediaInfo;
import nl.siwoc.mediainfo.riff.avi.AVIRIFF;

public class RIFFUtils {

	private static final Logger LOGGER = Logger.getLogger(RIFFUtils.class.getName());

	public static MediaInfo parse(String filename) throws Exception {
		LOGGER.info("Start parsing file: " + filename);
		try (FileInputStream fis = new FileInputStream(filename)) {
			String fourCC = RIFFUtils.readFourCC(fis);
			LOGGER.info("Found fourCC: [" + fourCC + "] should be [RIFF]");
			if (!"RIFF".equals(fourCC)) {
				throw new Exception("File is not RIFF");
			}
			int size = RIFFUtils.readIntLE(fis);
			String fileType = RIFFUtils.readFourCC(fis);
			LOGGER.info("Found fileType: [" + fileType + "]");
			if (!"AVI ".equals(fileType)) {
				throw new Exception("RIFF FileType: " + fileType + " is not supported");
			}
			return new AVIRIFF(fileType, size, fis);
		} catch (Exception e) {
			throw e;
		}
	}

	public static int readIntLE(InputStream is) throws Exception {
		byte[] bytes = new byte[4];
		if (is.read(bytes) != 4) throw new Exception("Bytes read != 4, could not readIntLE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	public static short readShortLE(InputStream is) throws Exception {
		byte[] bytes = new byte[2];
		if (is.read(bytes) != 2) throw new Exception("Bytes read != 2, could not readShortLE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	public static String readFourCC(InputStream is) throws Exception {
		byte[] bytes = new byte[4];
		if (is.read(bytes) != 4) throw new Exception("Bytes read != 4, could not readFourCC");
		return new String(bytes, "ASCII");
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

		String filename = "O:/downloads/Shazam (2019)/Shazam (2019).avi";
		MediaInfo mediaInfo = RIFFUtils.parse(filename);
		System.out.println(mediaInfo.getContainer());
		System.out.println(mediaInfo.getVideoCodec());
		System.out.println(mediaInfo.getFrameWidth());
		System.out.println(mediaInfo.getFrameHeight());
		System.out.println(mediaInfo.getAudioCodec());
		System.out.println(mediaInfo.getAudioChannels());
	}
	
}
