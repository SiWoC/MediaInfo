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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.siwoc.mediainfo.utils.ReadUtils;

public class Avc1SampleEntry extends SampleEntry {
	
	protected static final Logger LOG = LoggerFactory.getLogger(Avc1SampleEntry.class);
	
	private int width;
	private int height;
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Avc1SampleEntry(Box parent, long size, byte[] data) throws Exception {
		setType("avc1");
		setSize(size);
		setParent(parent);
		LOG.info("Creating " + getType());
        TrakBox trak = (TrakBox)searchUp("trak");
        if (trak != null) {
        	trak.setCodecId(getType());
        }
		try (InputStream is = new ByteArrayInputStream(data)){
			// SampleEntry base
			// skip 6
			ReadUtils.readUInt32BE(is);
			ReadUtils.readInt16BE(is);
			setDataReferenceIndex(ReadUtils.readUInt16BE(is));
			// VisualSampleEntry
			// skip 16 (0)
			ReadUtils.readUInt32BE(is);
			ReadUtils.readUInt32BE(is);
			ReadUtils.readUInt32BE(is);
			ReadUtils.readUInt32BE(is);
			setWidth(ReadUtils.readUInt16BE(is));
			setHeight(ReadUtils.readUInt16BE(is));
			if (trak != null) {
				trak.setWidth(getWidth());
				trak.setHeight(getHeight());
			}
			LOG.info(toString());
			
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	codingname{
		 unsigned int(16) pre_defined = 0;
		 const unsigned int(16) reserved = 0;
		 unsigned int(32)[3] pre_defined = 0;
		 unsigned int(16) width;
		 unsigned int(16) height;
		 template unsigned int(32) horizresolution = 0x00480000; // 72 dpi
		 template unsigned int(32) vertresolution = 0x00480000; // 72 dpi
		 const unsigned int(32) reserved = 0;
		 template unsigned int(16) frame_count = 1;
		 string[32] compressorname;
		 template unsigned int(16) depth = 0x0018;
		 int(16) pre_defined = -1;
		 */

	public String toString() {
		return "Avc1SampleEntry{ " +
	            "width=" + getWidth() +
	            ", height=" + getHeight() +
	            //", height=" + handlerType + 
	            " }";
	}
}
