/*******************************************************************************
 * Copyright (c) 2019 Niek Knijnenburg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
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
			return getVideoTrack().getVideo().getPixelWidth();
		}
		return 0;
	}

	@Override
	public int getFrameHeight() {
		if (getVideoTrack() != null) {
			return getVideoTrack().getVideo().getPixelHeight();
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
