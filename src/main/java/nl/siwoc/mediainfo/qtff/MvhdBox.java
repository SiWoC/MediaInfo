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

public class MvhdBox extends FullBox {

    private long creationTime;
    private long modificationTime;
    private int timescale;
    private long duration;

    public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(long modificationTime) {
		this.modificationTime = modificationTime;
	}

	public int getTimescale() {
		return timescale;
	}

	public void setTimescale(int timescale) {
		this.timescale = timescale;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public MvhdBox(Box parent, int size, byte[] data) throws Exception {
		setType("mvhd");
		setSize(size);
		setParent(parent);
		LOGGER.info("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(QTFFUtils.readFlag(is));
	        if (getVersion() == 1) {
	        	setCreationTime(QTFFUtils.readWideBE(is));
	        	setModificationTime(QTFFUtils.readWideBE(is));
	        	setTimescale(QTFFUtils.readIntBE(is));
	        	setDuration(QTFFUtils.readWideBE(is));
	        } else {
	        	setCreationTime(QTFFUtils.readIntBE(is));
	        	setModificationTime(QTFFUtils.readIntBE(is));
	        	setTimescale(QTFFUtils.readIntBE(is));
	        	setDuration(QTFFUtils.readIntBE(is));
	        }
	        LOGGER.info(toString());
	        // don't need rest at this moment
		} catch (Exception e){
			throw e;
		}
    }

	public String toString() {
		return "MvhdBox{" +
	            "version=" + getVersion() +
	            ", creationTime=" + getCreationTime() +
	            ", modificationTime=" + getModificationTime() +
	            ", timescale=" + getTimescale() +
	            ", duration=" + getDuration() +
	            " }";

	}
}
