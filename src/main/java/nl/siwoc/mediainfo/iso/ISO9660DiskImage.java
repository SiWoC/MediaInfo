package nl.siwoc.mediainfo.iso;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class ISO9660DiskImage {

	private ISO9660DiskImageFS rootFS;
	private RandomAccessFile raf;
	private static final int SECTORSIZE = 2048;

	public ISO9660DiskImageFS getRootFS() {
		return rootFS;
	}

	public void setRootFS(ISO9660DiskImageFS rootFS) {
		this.rootFS = rootFS;
	}

	public RandomAccessFile getRaf() {
		return raf;
	}

	public void setRaf(RandomAccessFile raf) {
		this.raf = raf;
	}

	public ISO9660DiskImage(String isopath) throws Exception {
		setRaf(new RandomAccessFile(isopath, "r"));
		readSector(16);
	}

	public void readSector(int nSector) throws Exception {
			getRaf().seek(SECTORSIZE * nSector);
			byte[] sector = new byte[SECTORSIZE];
			getRaf().read(sector);
			String header = getHeader(sector);
			if (!"CD001".equals(header) //) {
					&& !"BEA01".equals(header)) {
				throw new Exception("Could not find DVD header, unsupported disk image");
			}

			createRootFS(sector);
	}
			
	private String getHeader(byte[] sector) {
		return new String(Arrays.copyOfRange(sector, 1, 6));
	}

	private void createRootFS(byte[] data) throws Exception {
		int startSector = ISOUtils.readIntLE(data, 158);
		System.out.println(startSector);
		int size = ISOUtils.readIntLE(data, 166);
		System.out.println(size);
		setRootFS(new ISO9660DiskImageFS(startSector, size, "", true, getRaf()));
	}

	public boolean existsFile(String filePath) {
		return getRootFS().existsFile(filePath);
	}

	public ISO9660DiskImageFS getFile(String filePath) throws IOException {
		return getRootFS().getFile(filePath);
	}

	public void close() {
		try {
			getRaf().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
