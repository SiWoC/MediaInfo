package nl.siwoc.mediainfo.riff.avi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.siwoc.mediainfo.riff.ListChunk;
import nl.siwoc.mediainfo.riff.RIFFUtils;

public class StreamList extends ListChunk {

	private static final Logger LOGGER = Logger.getLogger(StreamList.class.getName());

	private StreamHeader strh;
	private StreamFormat strf;

	public StreamHeader getStrh() {
		return strh;
	}

	public void setStrh(StreamHeader strh) {
		this.strh = strh;
	}

	public StreamFormat getStrf() {
		return strf;
	}

	public void setStrf(StreamFormat strf) {
		this.strf = strf;
	}

	public StreamList(int size, byte[] data) throws Exception {
		setListType("strl");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(data)) {
			String fourCC;
			int childSize;
			byte[] childData;
			// expect strh
			fourCC = RIFFUtils.readFourCC(is);
			if (!"strh".equals(fourCC)) {
				throw new Exception("Invalid AVI StreamList, unable to find strh, found: " + fourCC);
			}
			childSize = RIFFUtils.readIntLE(is);
			childData = new byte[childSize];
			is.read(childData);
			setStrh(new StreamHeader(childSize, childData));
			// strf
			fourCC = RIFFUtils.readFourCC(is);
			if (!"strf".equals(fourCC)) {
				throw new Exception("Invalid AVI StreamList, unable to find strf, found: " + fourCC);
			}
			childSize = RIFFUtils.readIntLE(is);
			LOGGER.log(Level.FINE,"strfSize: " + childSize);
			childData = new byte[childSize];
			is.read(childData);
			if ("vids".equals(getStrh().getType())) {
				setStrf(new BitmapInfo(childSize, childData));
			} else if ("auds".equals(getStrh().getType())) {
				setStrf(WaveFormat.getInstance(childSize, childData));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public String getHandler() {
		if ("vids".equals(getStrh().getType())) {
			return getStrh().getHandler();
		} else if ("auds".equals(getStrh().getType())) {
			return getStrf().getHandler();
		} else {
			return "NNSL";
		}
	}

	public short getChannels() {
		if ("auds".equals(getStrh().getType())) {
			return getStrf().getChannels();
		} else {
			return 0;
		}
	}

}
