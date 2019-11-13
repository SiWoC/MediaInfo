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
import java.util.ArrayList;
import nl.siwoc.mediainfo.MediaInfo;
import nl.siwoc.mediainfo.utils.Logger;
import nl.siwoc.mediainfo.utils.ReadUtils;

public class QTFFUtils {

	public static MediaInfo parse(String filename) throws Exception {
		Logger.logInfo("Start parsing file: " + filename);
		try (FileInputStream fis = new FileInputStream(filename)) {
			long remaining = new File(filename).length();
			Box qtff = new Box();
			qtff.setType("root");
			try {
				while (remaining >= 4) {
					long atomSize = ReadUtils.readUInt32BE(fis);
					String fourCC = ReadUtils.readFourCC(fis);
					byte[] childData;
					remaining = remaining - atomSize;
					Logger.logInfo("Found fourCC: [" + fourCC + "] with size [" + atomSize + "]");
					// only interested in metadata, skipping everything else
					if (fourCC.equals("ftyp")) {
						childData = new byte[(int) (atomSize - 8)];
						fis.read(childData);
						qtff.addChild(new FtypBox(qtff, atomSize, childData));
					} else if (fourCC.equals("moov")) {
						childData = new byte[(int) (atomSize - 8)];
						fis.read(childData);
						qtff.addChild(new MoovBox(qtff, atomSize, childData));
					} else if (atomSize < 8) {
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
				Logger.logInfo("moov without ftyp = mov.mp41");
				qtff.addChild(new FtypBox("mp41", 0));
			}
			if (qtff.getChild("ftyp") == null) {
				throw new Exception("No [ftyp], file is not QTFF/MOV/MP4");
			}
			if (Logger.isInTrace()) {
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

	public static void main (String args[]) throws Exception {
		Logger.setLogLevel("TRACE");
		//String filename = "O:/Kinder films/Free Birds (2013)/Free Birds.mp4";
		//String filename = "N:/Casper/huiswerk/Film NL/Dood.MOV";
		String filename = "O:/Films/Glass (2019)/Glass (2019).mp4";
		MediaInfo mp4 = QTFFUtils.parse(filename);
		System.out.println(mp4.getContainer());
		System.out.println(mp4.getVideoCodec());
		Box qtff = ((MediaBox) mp4).getQtff();
		qtff.print(0);
		ArrayList<Box> ac3SampleEntries = qtff.getChildren("ac3");
		for (Box box : ac3SampleEntries) {
			Ac3SampleEntry ac3 = (Ac3SampleEntry) box;
			System.out.println(ac3.getSampleRate());
		}
	}

}
