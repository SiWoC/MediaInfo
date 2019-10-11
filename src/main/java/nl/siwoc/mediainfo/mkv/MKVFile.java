package nl.siwoc.mediainfo.mkv;

import java.util.logging.Logger;

import org.ebml.io.FileDataSource;
import org.ebml.matroska.MatroskaFile;
import org.ebml.matroska.MatroskaFileTrack;
import org.ebml.matroska.MatroskaFileTrack.TrackType;

import nl.siwoc.mediainfo.MediaInfo;

public class MKVFile extends MatroskaFile implements MediaInfo {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(MKVFile.class.getName());
	
	private MatroskaFileTrack videoTrack;
	private MatroskaFileTrack audioTrack;
	
	public MatroskaFileTrack getVideoTrack() {
		if (videoTrack == null) {
			MatroskaFileTrack[] tracks = getTrackList();
			for (MatroskaFileTrack track : tracks) {
				if (track.getTrackType() == TrackType.VIDEO && track.getVideo() != null) {
					this.videoTrack = track;
					return videoTrack;
				}
			}
		}
		return videoTrack;
	}

	public MatroskaFileTrack getAudioTrack() {
		if (audioTrack == null) {
			MatroskaFileTrack[] tracks = getTrackList();
			for (MatroskaFileTrack track : tracks) {
				if (track.getTrackType() == TrackType.AUDIO && track.getAudio() != null) {
					this.audioTrack = track;
					return audioTrack;
				}
			}
		}
		return audioTrack;
	}

	public MKVFile (String filename) throws Exception {
		super(new FileDataSource(filename));
		super.readFile();
	}

	@Override
	public String getContainer() {
		return "mkv";
	}

	@Override
	public String getVideoCodec() {
		if (getVideoTrack() != null) {
			return getVideoTrack().getCodecID().split("/")[0].substring(2).toLowerCase();
		}
		return null;
	}

	@Override
	public int getFrameWidth() {
		if (getVideoTrack() != null) {
			return getVideoTrack().getVideo().getDisplayWidth();
		}
		return 0;
	}

	@Override
	public int getFrameHeight() {
		if (getVideoTrack() != null) {
			return getVideoTrack().getVideo().getDisplayHeight();
		}
		return 0;
	}

	@Override
	public String getAudioCodec() {
		if (getAudioTrack() != null) {
			return getAudioTrack().getCodecID().split("/")[0].substring(2).toLowerCase();
		}
		return null;
	}

	@Override
	public short getAudioChannels() {
		if (getAudioTrack() != null) {
			return getAudioTrack().getAudio().getChannels();
		}
		return 0;
	}

}
