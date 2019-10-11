package nl.siwoc.mediainfo.qtff;

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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFrameHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAudioCodec() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getAudioChannels() {
		// TODO Auto-generated method stub
		return 0;
	}


}
