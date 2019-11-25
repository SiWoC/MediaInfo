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

public class VmhdBox extends FullBox {
	
	protected static final Logger LOG = LoggerFactory.getLogger(VmhdBox.class);

	public VmhdBox(Box parent, long size, byte[] data) throws Exception {
		setType("vmhd");
		setSize(size);
		setParent(parent);
		LOG.info("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(ReadUtils.readFlag(is));
	        LOG.info(toString());
	        // don't need rest at this moment
		} catch (Exception e){
			throw e;
		}
    }

	public String toString() {
		return "VmhdBox{" +
	            "version=" + getVersion() +
	            " }";
	}
}
