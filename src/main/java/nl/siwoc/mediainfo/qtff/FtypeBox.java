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
import java.util.ArrayList;
import java.util.logging.Logger;

import nl.siwoc.mediainfo.utils.ReadUtils;

public class FtypeBox extends Box {

	private static final Logger LOGGER = Logger.getLogger(FtypeBox.class.getName());
	
	private String majorBrand;
	private long minorVersion;
	private ArrayList<String> compBrands = new ArrayList<String>();

	public String getMajorBrand() {
		return majorBrand;
	}

	public void setMajorBrand(String majorBrand) {
		this.majorBrand = majorBrand;
	}

	public long getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(long minorVersion) {
		this.minorVersion = minorVersion;
	}

	public ArrayList<String> getCompBrands() {
		return compBrands;
	}

	public void setCompBrands(ArrayList<String> compBrands) {
		this.compBrands = compBrands;
	}

	public FtypeBox(long size, byte[] childData) throws Exception {
		setType("ftype");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(childData)){
			setMajorBrand(ReadUtils.readFourCC(is).trim());
			setMinorVersion(ReadUtils.readUInt32BE(is));
	        String brand;
	        while (is.available() >= 4 && (brand = ReadUtils.readFourCC(is)) != null) {
	            compBrands.add(brand);
	        }
		} catch (Exception e) {
			throw e;
		}
	}
	
	public FtypeBox(String majorBrand, int minorVersion) {
		setType("ftype");
		setMajorBrand(majorBrand);
		setMinorVersion(minorVersion);
	}

}
