package nl.siwoc.mediainfo.riff;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

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

	
}
