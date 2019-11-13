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

import nl.siwoc.mediainfo.utils.Logger;
import nl.siwoc.mediainfo.utils.ReadUtils;

public class MdhdBox extends FullBox {

    private BigDecimal creationTime;
    private BigDecimal modificationTime;
    private long timescale;
    private BigDecimal duration;

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

	public long getTimescale() {
		return timescale;
	}

	public void setTimescale(long timescale) {
		this.timescale = timescale;
	}

	public BigDecimal getDuration() {
		return duration;
	}

	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

	public MdhdBox(Box parent, long size, byte[] data) throws Exception {
		setType("mdhd");
		setSize(size);
		setParent(parent);
		Logger.logInfo("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(ReadUtils.readFlag(is));
	        if (getVersion() == 1) {
	        	setCreationTime(ReadUtils.readUInt64BE(is));
	        	setModificationTime(ReadUtils.readUInt64BE(is));
	        	setTimescale(ReadUtils.readUInt32BE(is));
	        	setDuration(ReadUtils.readUInt64BE(is));
	        } else {
	        	setCreationTime(new BigDecimal(ReadUtils.readUInt32BE(is)));
	        	setModificationTime(new BigDecimal(ReadUtils.readUInt32BE(is)));
	        	setTimescale(ReadUtils.readUInt32BE(is));
	        	setDuration(new BigDecimal(ReadUtils.readUInt32BE(is)));
	        }
	        Logger.logInfo(toString());
	        // don't need rest at this moment
		} catch (Exception e){
			throw e;
		}
    }

	public String toString() {
		return "MdhdBox{" +
	            "version=" + getVersion() +
	            ", creationTime=" + getCreationTime() +
	            ", modificationTime=" + getModificationTime() +
	            ", timescale=" + getTimescale() +
	            ", duration=" + getDuration() +
	            " }";
	}
}
