package nl.siwoc.mediainfo.riff.avi;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.siwoc.mediainfo.MediaInfo;
import nl.siwoc.mediainfo.riff.RIFFChunk;
import nl.siwoc.mediainfo.riff.RIFFUtils;

public class AVIRIFF extends RIFFChunk implements MediaInfo{

	private static final Logger LOGGER = Logger.getLogger(AVIRIFF.class.getName());
	
	private HeaderList hdrl;

	public HeaderList getHdrl() {
		return hdrl;
	}

	public void setHdrl(HeaderList headerList) {
		this.hdrl = headerList;
	}

	public AVIRIFF(String fileType, int size, InputStream is) throws Exception {
		setId("RIFF");
		setSize(size);
		setFileType(fileType);
		String fourCC;
		fourCC = RIFFUtils.readFourCC(is);
		if (!"LIST".equals(fourCC)) {
			throw new Exception("Invalid AVI RIFF, unable to find headerlist LIST, found: " + fourCC);
		}
		int hdrlSize = RIFFUtils.readIntLE(is);
		LOGGER.log(Level.FINE,"hdrlSize: " + hdrlSize);
		fourCC = RIFFUtils.readFourCC(is);
		if (!"hdrl".equals(fourCC)) {
			throw new Exception("Invalid AVI RIFF, unable to find headerlist hdrl, found: " + fourCC);
		}
		byte[] hdrlBytes = new byte[hdrlSize - 4];
		is.read(hdrlBytes);
		setHdrl(new HeaderList(hdrlSize, hdrlBytes));
		/* at the moment don't care about the rest
		fourCC = RIFFUtils.readFourCC(is);
		System.out.println("fourCC: " + fourCC);
		int LISTSize = RIFFUtils.readIntLE(is);
		System.out.println("LISTSize: " + LISTSize);
		String listType = RIFFUtils.readFourCC(is);
		System.out.println("listType: " + listType);
		byte[] bytes = new byte[1024];
		is.read(bytes);
		FileOutputStream fos = new FileOutputStream("c:/temp/avi.txt");
		fos.write(bytes);
		fos.close();
		System.out.println(new String(bytes, "ASCII"));
		*/
	}

	@Override
	public String getContainer() {
		return "avi";
	}

	@Override
	public String getVideoCodec() {
		if (getHdrl().getVideoStreams().size() > 0) {
			return getHdrl().getVideoStreams().get(0).getHandler().toLowerCase();
		} else {
			return null;
		}
	}

	@Override
	public int getFrameWidth() {
		return getHdrl().getAvih().getFrameWidth();
	}

	@Override
	public int getFrameHeight() {
		return getHdrl().getAvih().getFrameHeight();
	}

	@Override
	public String getAudioCodec() {
		if (getHdrl().getAudioStreams().size() > 0)
			return getHdrl().getAudioStreams().get(0).getHandler().toLowerCase();
		else {
			return null;
		}
	}

	@Override
	public short getAudioChannels() {
		if (getHdrl().getAudioStreams().size() > 0)
			return getHdrl().getAudioStreams().get(0).getChannels();
		else {
			return 0;
		}
	}

}
