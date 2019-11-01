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

public class TrakBox extends Box {

    private String handlerType;
    private String codecId;
	private short width;
	private short height;
	private short channelCount;
	
    public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	public String getCodecId() {
		return codecId;
	}

	public void setCodecId(String codecId) {
		this.codecId = codecId;
	}

	public short getWidth() {
		return width;
	}

	public void setWidth(short width) {
		this.width = width;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public short getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(short channelCount) {
		this.channelCount = channelCount;
	}

	public TrakBox(Box parent, int size, byte[] data) throws Exception {
		setType("trak");
		setSize(size);
		setParent(parent);
		parseChildren(this, size, data);
	}

	public String toString() {
		return "TrakBox{ " +
	            "handlerType=" + getHandlerType() + 
	            ", codecId=" + getCodecId() + 
	            " }";
	}

}
