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
import java.math.BigDecimal;

import nl.siwoc.mediainfo.utils.ReadUtils;

public class TkhdBox extends FullBox {

    private BigDecimal creationTime;
    private BigDecimal modificationTime;
    private long trackId;
    private BigDecimal duration;
    private short layer;
    private short alternateGroup;
    private short volume;
    private int[] matrix = new int[9];
    private long width;
    private long height;    

    public BigDecimal getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(BigDecimal creationTime) {
		this.creationTime = creationTime;
	}

	public BigDecimal getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(BigDecimal modificationTime) {
		this.modificationTime = modificationTime;
	}

	public long getTrackId() {
		return trackId;
	}

	public void setTrackId(long trackId) {
		this.trackId = trackId;
	}

	public BigDecimal getDuration() {
		return duration;
	}

	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

	public short getLayer() {
		return layer;
	}

	public void setLayer(short layer) {
		this.layer = layer;
	}

	public short getAlternateGroup() {
		return alternateGroup;
	}

	public void setAlternateGroup(short alternateGroup) {
		this.alternateGroup = alternateGroup;
	}

	public short getVolume() {
		return volume;
	}

	public void setVolume(short volume) {
		this.volume = volume;
	}

	public int[] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[] matrix) {
		this.matrix = matrix;
	}

	private void setMatrix(int i, int k) {
		this.matrix[i] = k;
	}

	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public TkhdBox(Box parent, long size, byte[] data) throws Exception {
		setType("tkhd");
		setSize(size);
		setParent(parent);
		LOGGER.info("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(ReadUtils.readFlag(is));
	        if (getVersion() == 1) {
	        	setCreationTime(ReadUtils.readUInt64BE(is));
	        	setModificationTime(ReadUtils.readUInt64BE(is));
	        	setTrackId(ReadUtils.readUInt32BE(is));
	        	ReadUtils.readUInt32BE(is); //reserved
	        	setDuration(ReadUtils.readUInt64BE(is));
	        } else {
	        	setCreationTime(new BigDecimal(ReadUtils.readUInt32BE(is)));
	        	setModificationTime(new BigDecimal(ReadUtils.readUInt32BE(is)));
	        	setTrackId(ReadUtils.readUInt32BE(is));
	        	ReadUtils.readUInt32BE(is); //reserved
	        	setDuration(new BigDecimal(ReadUtils.readUInt32BE(is)));
	        }
        	ReadUtils.readUInt32BE(is); //reserved [0]
        	ReadUtils.readUInt32BE(is); //reserved [1]
        	setLayer(ReadUtils.readInt16BE(is));
        	setAlternateGroup(ReadUtils.readInt16BE(is));
        	setVolume(ReadUtils.readInt16BE(is));
        	ReadUtils.readUInt16BE(is); // reserved
        	for (int i = 0 ; i < matrix.length ; i++) {
        		setMatrix(i,ReadUtils.readInt32BE(is));
        	}
        	setWidth(ReadUtils.readUInt32BE(is));
        	setHeight(ReadUtils.readUInt32BE(is));
	        LOGGER.info(toString());
	        // don't need rest at this moment
		} catch (Exception e){
			throw e;
		}
    }

	public String toString() {
		return "TkhdBox{" +
	            "version=" + getVersion() +
	            ", creationTime=" + getCreationTime() +
	            ", modificationTime=" + getModificationTime() +
	            ", trackId=" + getTrackId() +
	            ", duration=" + getDuration() +
	            ", layer=" + getLayer() +
	            ", altgroup=" + getAlternateGroup() +
	            ", volume=" + getVolume() +
	            ", width=" + getWidth() +
	            ", height=" + getHeight() +
	            " }";
	}
}
