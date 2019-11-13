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

import nl.siwoc.mediainfo.utils.Logger;
import nl.siwoc.mediainfo.utils.ReadUtils;

public class HdlrBox extends FullBox {

    private String componentType;
    private String handlerType;

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	public HdlrBox(Box parent, long size, byte[] data) throws Exception {
		setType("hdlr");
		setSize(size);
		setParent(parent);
		Logger.logInfo("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(ReadUtils.readFlag(is));
	        setComponentType(ReadUtils.readFourCC(is));
	        setHandlerType(ReadUtils.readFourCC(is));
	        if (getHandlerType().matches("vide|soun")) {
		        TrakBox trak = (TrakBox)searchUp("trak");
		        if (trak != null) {
		        	trak.setHandlerType(getHandlerType());
		        }
	        }
	        Logger.logInfo(toString());
	        // don't need rest at this moment
		} catch (Exception e){
			throw e;
		}
    }
	
	public String toString() {
		return "HdlrBox{ " +
	            "version=" + getVersion() +
	            ", componentType=" + componentType +
	            ", handlerType=" + handlerType + " }";
	}

}
