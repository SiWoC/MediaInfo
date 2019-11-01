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
package nl.siwoc.mediainfo.qtff;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

public class QTFFUtils {

	private static final Logger LOGGER = Logger.getLogger(QTFFUtils.class.getName());

	public static MediaInfo parse(String filename) throws Exception {
		LOGGER.info("Start parsing file: " + filename);
		try (FileInputStream fis = new FileInputStream(filename)) {
			long remaining = new File(filename).length();
			Box qtff = new Box();
			qtff.setType("root");
			try {
				while (remaining >= 4) {
					int atomSize = QTFFUtils.readIntBE(fis);
					String fourCC = QTFFUtils.readFourCC(fis);
					byte[] childData;
					remaining = remaining - atomSize;
					LOGGER.info("Found fourCC: [" + fourCC + "] with size [" + atomSize + "]");
					// only interested in metadata, skipping everything else
					if (fourCC.equals("ftyp")) {
						childData = new byte[atomSize - 8];
						fis.read(childData);
						qtff.addChild(new FtypBox(qtff, atomSize, childData));
					} else if (fourCC.equals("moov")) {
						childData = new byte[atomSize - 8];
						fis.read(childData);
						FileOutputStream fos = new FileOutputStream("c:/temp/birdmoov.txt");
						fos.write(0); fos.write(0); fos.write(0); fos.write(atomSize);
						fos.write("moov".getBytes());
						fos.write(childData);
						fos.close();
						qtff.addChild(new MoovBox(qtff, atomSize, childData));
					} else if (atomSize < 9) {
						throw new Exception("Invalid atomSize: [" + atomSize + "], file is not QTFF/MOV/MP4");
					} else {
						fis.skip(atomSize - 8);
					}
				}
			} catch (Exception e) {
				if (!(e instanceof EOFException))
				{
					throw e;
				}
			}
			if (qtff.getChild("moov") != null && qtff.getChild("ftyp") == null) {
				LOGGER.info("moov without ftyp = mov.mp41");
				qtff.addChild(new FtypBox("mp41", 0));
			}
			if (qtff.getChild("ftyp") == null) {
				throw new Exception("No [ftyp], file is not QTFF/MOV/MP4");
			}
			if (LOGGER.getLevel() == Level.FINER) {
				qtff.print(0);
			}
			MediaBox mb = new MediaBox(qtff);
			if (mb.getContainer() == null) {
				throw new Exception("QTFF unknown container");
			}
			return mb;
		} catch (Exception e) {
			throw e;
		}
	}

	public static int readIntBE(InputStream is) throws Exception {
		byte[] bytes = new byte[4];
		if (is.read(bytes) != 4) throw new Exception("Bytes read != 4, could not readIntBE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
	}

	public static String readFourCC(InputStream is) throws Exception {
		byte[] bytes = new byte[4];
		if (is.read(bytes) != 4) throw new Exception("Bytes read != 4, could not readFourCC");
		return new String(bytes, "ASCII");
	}

	public static long readWideBE(InputStream is) throws Exception {
		byte[] bytes = new byte[8];
		if (is.read(bytes) != 8) throw new Exception("Bytes read != 8, could not readWideBE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getLong();
	}
	
	public static byte[] readFlag(InputStream is) throws Exception {
		byte[] bytes = new byte[3];
		if (is.read(bytes) != 3) throw new Exception("Bytes read != 3, could not readWideBE");
		return bytes;
	}

	public static short readShortBE(InputStream is) throws Exception {
		byte[] bytes = new byte[2];
		if (is.read(bytes) != 2) throw new Exception("Bytes read != 2, could not readShortBE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort();
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
		String filename = "O:/Kinder films/Free Birds (2013)/Free Birds.mp4";
		//String filename = "N:/Casper/huiswerk/Film NL/Dood.MOV";
		MediaInfo mp4 = QTFFUtils.parse(filename);
		System.out.println(mp4.getContainer());
		System.out.println(mp4.getVideoCodec());
	}

}
