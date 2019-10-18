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
