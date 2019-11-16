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
		} else if (majorBrand.matches("isom|mpg4|mp41|mp42|mp43")) {
			return "mp4";
		}
		return majorBrand.trim();
	}

	@Override
	public String getVideoCodec() {
		String videoCodec = ((FtypBox) qtff.getChild("ftyp")).getMajorBrand();
		ArrayList<String> compBrands =  ((FtypBox) qtff.getChild("ftyp")).getCompBrands();
		if ("isom".equalsIgnoreCase(videoCodec) && compBrands != null && compBrands.size() > 0) {
			return compBrands.get(0);
		} else {
			return videoCodec;
		}
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
	public int getAudioChannels() {
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
