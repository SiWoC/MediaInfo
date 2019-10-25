package nl.siwoc.mediainfo.iso;

import java.io.File;
import java.io.IOException;
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
		DVDFile dvdFile = null;
		try {
			f = new ISO9660DiskImage(filename);
			for (int i = 1 ; i < 100 ; i++) {
				String vtsFilename = "VIDEO_TS" + System.getProperty("file.separator") + "VTS_" + String.format("%02d", i) + "_0.IFO";
				LOGGER.info("Searching vts-file: " + vtsFilename);
				byte[] vtsFileData = f.getFile(vtsFilename);
				if (vtsFileData.length == 0) {
					throw new Exception("No DVD ISO file [VTS_xx_0.IFO] found.");
				}
				dvdFile = DVDFile.parseFromByteArray(vtsFileData);
				dvdFile.setContainer("dvdiso");
				if (dvdFile.getNumberOfAudioStreams() > 0) {
					return dvdFile;
				}
				LOGGER.info("vts-file: " + vtsFilename + " has no AudioStreams searching next IFO");
			}
			return dvdFile;
			//throw new Exception("No DVD ISO file [VTS_xx_0.IFO] found.");
		} catch (Exception e) {
			throw e;
		} finally {
			if (f != null) {
				f.close();
			}
		}
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

	public static int readLittleEndianWord(byte[] bytes) {
		byte b1 = bytes[0];
		byte b2 = bytes[1];
		byte b3 = bytes[2];
		byte b4 = bytes[3];
		int s = 0;
		s |= b4 & 0xFF;
		s <<= 8;
		s |= b3 & 0xFF;
		s <<= 8;
		s |= b2 & 0xFF;
		s <<= 8;
		s |= b1 & 0xFF;
		return s;
	}

	public static short UValue(byte b) {
		return (short) (b & 0xFF);
	}
	
}
