package nl.siwoc.mediainfo.qtff;

import java.util.ArrayList;

import nl.siwoc.mediainfo.MediaInfo;

public class MediaBox implements MediaInfo{

	private Box qtff;
	
	public Box getQtff() {
		return qtff;
	}

	public void setQtff(Box qtff) {
		this.qtff = qtff;
	}

	public MediaBox(Box qtff) throws Exception {
		setQtff(qtff);
	}

	@Override
	public String getContainer() {
		String majorBrand = ((FtypBox) qtff.getChild("ftyp")).getMajorBrand();
		if (majorBrand.matches("qt|mov")) {
			return "mov";
		} else if (majorBrand.matches("mpg4|mp41|mp42|mp43")) {
			return "mp4";
		}
		return null;
	}

	@Override
	public String getVideoCodec() {
		return ((FtypBox) qtff.getChild("ftyp")).getMajorBrand();
	}

	@Override
	public int getFrameWidth() {
		ArrayList<Box> traks = qtff.getChild("moov").getChildren("trak");
		for (Box box : traks) {
			if (box instanceof TrakBox) {
				TrakBox trak = (TrakBox)box;
				if (trak.getHandlerType().equals("vide")) {
					return trak.getWidth();
				}
			}
			
		}
		return 0;
	}

	@Override
	public int getFrameHeight() {
		ArrayList<Box> traks = qtff.getChild("moov").getChildren("trak");
		for (Box box : traks) {
			if (box instanceof TrakBox) {
				TrakBox trak = (TrakBox)box;
				if (trak.getHandlerType().equals("vide")) {
					return trak.getHeight();
				}
			}
			
		}
		return 0;
	}

	@Override
	public String getAudioCodec() {
		ArrayList<Box> traks = qtff.getChild("moov").getChildren("trak");
		for (Box box : traks) {
			if (box instanceof TrakBox) {
				TrakBox trak = (TrakBox)box;
				if (trak.getHandlerType().equals("soun")) {
					return trak.getCodecId();
				}
			}
			
		}
		return null;
	}

	@Override
	public short getAudioChannels() {
		ArrayList<Box> traks = qtff.getChild("moov").getChildren("trak");
		for (Box box : traks) {
			if (box instanceof TrakBox) {
				TrakBox trak = (TrakBox)box;
				if (trak.getHandlerType().equals("soun")) {
					return trak.getChannelCount();
				}
			}
			
		}
		return 0;
	}


}
