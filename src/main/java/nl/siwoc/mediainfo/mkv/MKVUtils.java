package nl.siwoc.mediainfo.mkv;

import java.util.logging.Logger;

import nl.siwoc.mediainfo.MediaInfo;

public class MKVUtils {

	private static final Logger LOGGER = Logger.getLogger(MKVUtils.class.getName());

	public static MediaInfo parse(String filename) throws Exception {
		LOGGER.info("Start parsing file: " + filename);
		return new MKVFile(filename);
	}

}
