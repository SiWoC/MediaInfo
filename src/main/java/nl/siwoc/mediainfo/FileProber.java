package nl.siwoc.mediainfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import nl.siwoc.mediainfo.dvd.DVDUtils;
import nl.siwoc.mediainfo.iso.ISOUtils;
import nl.siwoc.mediainfo.mkv.MKVUtils;
import nl.siwoc.mediainfo.qtff.QTFFUtils;
import nl.siwoc.mediainfo.riff.RIFFUtils;

public class FileProber {

	private static final Logger LOGGER = Logger.getLogger(FileProber.class.getName());

	public static void main(String[] args) {
		try {
			new File("log").mkdir();
			FileHandler handler = new FileHandler("log/FileProber.log", 500000, 2, true);
			handler.setFormatter(new SimpleFormatter());
			Logger.getLogger("").addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//String filename = "O:/downloads/Shazam (2019)/Shazam (2019).avi";
		//String filename = "O:/Series/Red Dwarf/S01/Red Dwarf S01E01 - The End.avi";
		//String filename = "O:/downloads/Aladdin (1992)/Aladdin (1992).mkv";
		//String filename = "O:\\downloads\\The Throwaways (2015)\\Vectronic Presents _ The Throwaways (2015) 720p.WEB-DL.AC3_x264. NL Subs Ingebakken\\The Throwaways (2015) 720p.WEB-DL.AC3_x264.avi";
		//ISO DVD not zip
		String filename = "O:/Kinder films/Early Man (2018)/Early Man (2018).iso";
		//ISO BR with zip
		//String filename = "O:/Kinder films/The Pirates Band of Misfits (2012)/The Pirates Band of Misfits (2012) 3D.iso";
		//ISO BR not zip
		//String filename = "O:/downloads/Hotel Transylvania 3 Summer Vacation (2018)/Hotel Transylvania 3 Summer Vacation (2018).iso";
		FileProber fp = new FileProber();
		FileProber.setLogLevel(Level.FINER);
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			LOGGER.log(Level.INFO,mediaInfo.getClass().getSimpleName() + System.lineSeparator() + 
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
			LOGGER.log(Level.INFO, e.getMessage());
		}
		try {
			return RIFFUtils.parse(filename);
		} catch (Exception e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		try {
			return MKVUtils.parse(filename);
		} catch (Exception e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		try {
			return QTFFUtils.parse(filename);
		} catch (Exception e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		try {
			return ISOUtils.parse(filename);
		} catch (Exception e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		
		try (FileInputStream fis = new FileInputStream(filename)){
			System.out.println(new File(filename).length());
			byte[] b = new byte[500000];
			//fis.skip(32768); // 16 sectors of ISO9660 DiskImage
			fis.read(b);
			fis.close();
			FileOutputStream fos = new FileOutputStream("c:/temp/mediainfo/unknownfile.txt");
			fos.write(b);
			fos.close();
			//System.out.println(new String(b, "ASCII"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}

	public static void setLogLevel(Level level) {
		Logger rootLogger = LogManager.getLogManager().getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		rootLogger.setLevel(level);
		for (Handler h : handlers) {
			if(h instanceof FileHandler)
				h.setLevel(level);
		}
	}
}
