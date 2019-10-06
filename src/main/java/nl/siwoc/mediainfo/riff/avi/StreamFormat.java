package nl.siwoc.mediainfo.riff.avi;

import nl.siwoc.mediainfo.riff.Chunk;

public abstract class StreamFormat extends Chunk {

	protected abstract String getHandler();

	protected abstract short getChannels();
	

}
