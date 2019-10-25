package nl.siwoc.mediainfo.dvd;

import java.io.File;
import java.io.FilenameFilter;
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

public class DVDUtils {

	private static final Logger LOGGER = Logger.getLogger(DVDUtils.class.getName());

	public static MediaInfo parse(String filename) throws Exception {
		LOGGER.info("Start parsing file: " + filename);
		File file = new File(filename);
		System.out.println(file.getName());
		if (file.getName().matches("VTS_\\d\\d_0.IFO")) {
			System.out.println("Found DVD-file at: " + file.getAbsolutePath());
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
		DVDFile dvdFile = null;
		File[] vtsFiles = videoTsFolder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.matches("VTS_\\d\\d_0.IFO");
			}
		});
		for (File file : vtsFiles) {
			if (file.isFile()) {
				dvdFile = DVDFile.parseFromFile(file.getAbsolutePath());
				if (dvdFile.getNumberOfAudioStreams() > 0) {
					return dvdFile;
				}
			}
		}
		return dvdFile;
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
		//String filename = "O:\\Kinder films\\Flight of the Navigator (1986)\\VIDEO_TS\\VTS_01_0.IFO";
		String filename = "O:/Kinder films/G-Force (2009)/VIDEO_TS/VTS_07_0.IFO";
		MediaInfo dvd = DVDUtils.parse(filename);
		System.out.println(dvd.getContainer());
		System.out.println(dvd.getVideoCodec());
	}
	
	public static int getSingleBit(int b, int position)
	{
	   return (b >> position) & 1;
	}

	public static int getTwoBits(int b, int position)
	{
	   return (b >> position) & 3;
	}

	public static int getThreeBits(int b, int position)
	{
	   return (b >> position) & 7;
	}

	public static short readShortBE(InputStream is) throws Exception {
		byte[] bytes = new byte[2];
		if (is.read(bytes) != 2) throw new Exception("Bytes read != 2, could not readShortBE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort();
	}
	
}
