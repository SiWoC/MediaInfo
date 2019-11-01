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

public class FullBox extends Box {
    private int version;
    private byte[] flag;
	
	public int getVersion() {
		return version;
	}
	
    public void setVersion(int version) {
		this.version = version;
	}
	
    public byte[] getFlag() {
		return flag;
	}
	
    public void setFlag(byte[] flag) {
		this.flag = flag;
	}

}
