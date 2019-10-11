package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class VmhdBox extends FullBox {

	public VmhdBox(int size, byte[] data) throws Exception {
		setType("vmhd");
		setSize(size);
		LOGGER.info("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(QTFFUtils.readFlag(is));
	        LOGGER.info(toString());
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
