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
package nl.siwoc.mediainfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.siwoc.mediainfo.dvd.DVDUtils;
import nl.siwoc.mediainfo.iso.ISOUtils;
import nl.siwoc.mediainfo.mkv.MKVUtils;
import nl.siwoc.mediainfo.qtff.QTFFUtils;
import nl.siwoc.mediainfo.riff.RIFFUtils;

public class FileProber {

	protected static final Logger LOG = LoggerFactory.getLogger(FileProber.class);
	
	public static void main(String[] args) {
		String filename = "O:/downloads/Shazam (2019)/Shazam (2019).avi";
		//String filename = "O:/Series/Red Dwarf/S01/Red Dwarf S01E01 - The End.avi";
		//String filename = "O:/downloads/Aladdin (1992)/Aladdin (1992).mkv";
		//String filename = "O:\\downloads\\The Throwaways (2015)\\Vectronic Presents _ The Throwaways (2015) 720p.WEB-DL.AC3_x264. NL Subs Ingebakken\\The Throwaways (2015) 720p.WEB-DL.AC3_x264.avi";
		//ISO DVD not zip
		//String filename = "O:/Kinder films/Early Man (2018)/Early Man (2018).iso";
		//ISO BR with zip
		//String filename = "O:/Kinder films/The Pirates Band of Misfits (2012)/The Pirates Band of Misfits (2012) 3D.iso";
		//ISO BR not zip
		//String filename = "O:/downloads/Hotel Transylvania 3 Summer Vacation (2018)/Hotel Transylvania 3 Summer Vacation (2018).iso";
		FileProber fp = new FileProber();
		//LOG.setLogLevel("DEBUG");
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			LOG.info(mediaInfo.getClass().getSimpleName() + System.lineSeparator() + 
					"  container: [" + mediaInfo.getContainer() + "]" + System.lineSeparator() +
					"  videocodec: " + mediaInfo.getVideoCodec() + System.lineSeparator() +
					"  width: " + mediaInfo.getFrameWidth() + System.lineSeparator() +
					"  height: " + mediaInfo.getFrameHeight() + System.lineSeparator() +
					"  audiocodec: " + mediaInfo.getAudioCodec() + System.lineSeparator() +
					"  audiochannels: " + mediaInfo.getAudioChannels()
					);
		}
	}
	
	public MediaInfo getMediaInfo(String filename) {
		try {
			return DVDUtils.parse(filename);
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
		try {
			return RIFFUtils.parse(filename);
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
		try {
			return MKVUtils.parse(filename);
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
		try {
			return QTFFUtils.parse(filename);
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
		try {
			return ISOUtils.parse(filename);
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
		
		try (FileInputStream fis = new FileInputStream(filename)){
			LOG.trace("Unknown filetype of length: " + new File(filename).length());
			byte[] b = new byte[500000];
			//fis.skip(32768); // 16 sectors of ISO9660 DiskImage
			fis.read(b);
			fis.close();
			FileOutputStream fos = new FileOutputStream("log/unknownfile.txt");
			fos.write(b);
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}

}
