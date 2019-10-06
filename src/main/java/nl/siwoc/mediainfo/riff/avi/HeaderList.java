package nl.siwoc.mediainfo.riff.avi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.siwoc.mediainfo.riff.ListChunk;
import nl.siwoc.mediainfo.riff.RIFFUtils;

public class HeaderList extends ListChunk {

	private static final Logger LOGGER = Logger.getLogger(HeaderList.class.getName());
	
	private MainHeader avih;
	private StreamList strl;
	private ArrayList<StreamList> videoStreams = new ArrayList<StreamList>();
	private ArrayList<StreamList> audioStreams = new ArrayList<StreamList>();

	public MainHeader getAvih() {
		return avih;
	}

	public void setAvih(MainHeader avih) {
		this.avih = avih;
	}

	public StreamList getStrl() {
		return strl;
	}

	public void setStrl(StreamList strl) {
		this.strl = strl;
	}

	public ArrayList<StreamList> getVideoStreams() {
		return videoStreams;
	}

	public void setVideoStreams(ArrayList<StreamList> videoStreams) {
		this.videoStreams = videoStreams;
	}

	public ArrayList<StreamList> getAudioStreams() {
		return audioStreams;
	}

	public void setAudioStreams(ArrayList<StreamList> audioStreams) {
		this.audioStreams = audioStreams;
	}

	public HeaderList(int size, byte[] data) throws Exception {
		setListType("hdrl");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(data)){
			String fourCC;
			// expect avih
			fourCC = RIFFUtils.readFourCC(is);
			if (!"avih".equals(fourCC)) {
				throw new Exception("Invalid AVI HeaderList, should start with avih, found: " + fourCC);
			}
			// read and discard size = 56
			int avihSize = RIFFUtils.readIntLE(is);
			byte[] avihBytes = new byte[avihSize];
			is.read(avihBytes);
			setAvih(new MainHeader(avihSize, avihBytes));
			for (int i = 0 ; i < getAvih().getNumberOfStreams() ; i++) {
				// expect LIST
				fourCC = RIFFUtils.readFourCC(is);
				if (!"LIST".equals(fourCC)) {
					throw new Exception("Invalid AVI HeaderList, unable to find header/streamlist LIST[" + i + "], found: " + fourCC);
				}
				int strlSize = RIFFUtils.readIntLE(is);
				LOGGER.log(Level.FINE,"strlSize: " + strlSize);
				// expect strl
				fourCC = RIFFUtils.readFourCC(is);
				if (!"strl".equals(fourCC)) {
					throw new Exception("Invalid AVI HeaderList, unable to find header/streamlist strl[\" + i + \"], found: " + fourCC);
				}
				byte[] strlBytes = new byte[strlSize - 4];
				is.read(strlBytes);
				StreamList strl = new StreamList(strlSize, strlBytes);
				String streamType = strl.getStrh().getType();
				if ("vids".equals(streamType)) {
					videoStreams.add(strl);
				} else if ("auds".equals(streamType)) {
					audioStreams.add(strl);
				} else {
					
				}
			}
		} catch (Exception e) {
			throw e;
		}

	}

}
