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

public class StsdBox extends FullBox {

    private int entryCount;
    private ArrayList<SampleEntry> sampleEntries = new ArrayList<SampleEntry>();

	public int getEntryCount() {
		return entryCount;
	}

	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}

	public ArrayList<SampleEntry> getSampleEntries() {
		return sampleEntries;
	}

	public void setSampleEntries(ArrayList<SampleEntry> sampleEntries) {
		this.sampleEntries = sampleEntries;
	}
	
	public void addSampleEntry(SampleEntry entry) {
		sampleEntries.add(entry);
	}

	public StsdBox(Box parent, int size, byte[] data) throws Exception {
		setType("stsd");
		setSize(size);
		setParent(parent);
		LOGGER.info("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(QTFFUtils.readFlag(is));
	        setEntryCount(QTFFUtils.readIntBE(is));
	        LOGGER.info(toString());
	        //for (int i = 0 ; i < entryCount ; i++)
	        // only reading 1 sample
	        {
	        	int entrySize = QTFFUtils.readIntBE(is);
	        	String entryType = QTFFUtils.readFourCC(is);
	        	byte[] entryData = new byte[entrySize - 8];
	        	is.read(entryData);
	        	addSampleEntry(createSampleEntry(this, entrySize, entryType, entryData));
	        }
	        // don't need rest at this moment
		} catch (Exception e){
			throw e;
		}
    }

	public String toString() {
		return "StsdBox{" +
	            "version=" + getVersion() +
	            ", entryCount=" + getEntryCount() +
	            " }";
	}
    
}
