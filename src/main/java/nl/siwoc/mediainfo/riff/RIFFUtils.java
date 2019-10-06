package nl.siwoc.mediainfo.riff;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import nl.siwoc.mediainfo.riff.avi.AVIRIFF;

public class RIFFUtils {

	public static RIFFChunk parse(String filename) throws Exception {
		try (FileInputStream fis = new FileInputStream(filename)) {
			String fourCC = RIFFUtils.readFourCC(fis);
			if (!"RIFF".equals(fourCC)) {
				throw new Exception("File is not RIFF");
			}
			int size = RIFFUtils.readIntLE(fis);
			String fileType = RIFFUtils.readFourCC(fis);
			if (!"AVI ".equals(fileType)) {
				throw new Exception("RIFF FileType: " + fileType + " is not supported");
			}
			return new AVIRIFF(size, fis);
		} catch (Exception e) {
			throw e;
		}
	}

	public static int readIntLE(InputStream is) throws IOException {
		byte[] bytes = new byte[4];
		is.read(bytes);
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	public static short readShortLE(InputStream is) throws IOException {
		byte[] bytes = new byte[2];
		is.read(bytes);
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	public static String readFourCC(InputStream is) throws IOException {
		byte[] bytes = new byte[4];
		is.read(bytes);
		return new String(bytes, "ASCII");
	}

	
}
