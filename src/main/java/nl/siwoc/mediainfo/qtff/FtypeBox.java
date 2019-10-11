package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

public class FtypeBox extends Box {

	private static final Logger LOGGER = Logger.getLogger(FtypeBox.class.getName());
	
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

	public FtypeBox(int size, byte[] childData) throws Exception {
		setType("ftype");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(childData)){
			setMajorBrand(QTFFUtils.readFourCC(is).trim());
			setMinorVersion(QTFFUtils.readIntBE(is));
	        String brand;
	        while (is.available() >= 4 && (brand = QTFFUtils.readFourCC(is)) != null) {
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
