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

import nl.siwoc.mediainfo.mkv.MKVFile;
import nl.siwoc.mediainfo.riff.RIFFChunk;
import nl.siwoc.mediainfo.riff.RIFFUtils;

public class FileProber {

	private static final Logger LOGGER = Logger.getLogger(FileProber.class.getName());

	public static void main(String[] args) {
		try {
			new File("log").mkdir();
			FileHandler handler = new FileHandler("log/FileProber.log", 50000, 2, true);
			handler.setFormatter(new SimpleFormatter());
			Logger.getLogger("").addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//String filename = "O:/downloads/Shazam (2019)/Shazam (2019).avi";
		//String filename = "O:/Series/Red Dwarf/S01/Red Dwarf S01E01 - The End.avi";
		String filename = "O:/downloads/Aladdin (1992)/Aladdin (1992).mkv";
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
		MediaInfo mediaInfo = null;
		try {
			RIFFChunk riff = RIFFUtils.parse(filename);
			if (riff instanceof MediaInfo) {
				return (MediaInfo)riff;
			} else {
				LOGGER.log(Level.INFO, "RIFF but not MediaInfo: " + riff.getClass().getName() + " - fileType: [" + riff.getFileType() + "] size: " + riff.getSize());
			}
		} catch (Exception e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		try {
			return new MKVFile(filename);
		} catch (Exception e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		if (mediaInfo == null) {
			try (FileInputStream fis = new FileInputStream(filename)){
				System.out.println(fis.available());
				byte[] b = new byte[10000];
				fis.read(b);
				fis.close();
				FileOutputStream fos = new FileOutputStream("log/mediafilenew.txt");
				fos.write(b);
				fos.close();
				System.out.println(new String(b, "ASCII"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mediaInfo;

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
