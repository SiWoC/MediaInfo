package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class FtypBox extends Box {

	private String majorBrand;
	private int minorVersion;
	private ArrayList<String> compBrands = new ArrayList<String>();

	public String getMajorBrand() {
		return majorBrand;
	}

	public void setMajorBrand(String majorBrand) {
		this.majorBrand = majorBrand;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}

	public ArrayList<String> getCompBrands() {
		return compBrands;
	}

	public void setCompBrands(ArrayList<String> compBrands) {
		this.compBrands = compBrands;
	}

	public FtypBox(Box parent, int size, byte[] data) throws Exception {
		setType("ftyp");
		setSize(size);
		setParent(parent);
		try (InputStream is = new ByteArrayInputStream(data)){
			setMajorBrand(QTFFUtils.readFourCC(is).trim());
			LOGGER.info("Ftyp has majorBrand: [" + getMajorBrand() + "]");
			setMinorVersion(QTFFUtils.readIntBE(is));
	        String brand;
	        while (is.available() >= 4 && (brand = QTFFUtils.readFourCC(is)) != null) {
	            compBrands.add(brand);
	        }
		} catch (Exception e) {
			throw e;
		}
	}
	
	public FtypBox(String majorBrand, int minorVersion) {
		setType("ftyp");
		setMajorBrand(majorBrand);
		LOGGER.info("Ftyp has majorBrand: [" + getMajorBrand() + "]");
		setMinorVersion(minorVersion);
	}

	public String toString() {
		return "FtypBox{ " +
	            " majorBrand=" + getMajorBrand() + 
	            ", minorVersion=" + getMinorVersion() + 
	            ", compBrands=" + getCompBrands() + 
	            " }";
	}

	
}
