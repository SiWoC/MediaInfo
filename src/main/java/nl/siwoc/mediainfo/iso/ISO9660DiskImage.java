package nl.siwoc.mediainfo.iso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;

public class ISO9660DiskImage {
	private String isopath;
	private ISO9660DiskImageFS rootFS;
	private RandomAccessFile raf;
	private static final int SECTORSIZE = 2048;

	public ISO9660DiskImage(String isopath) throws Exception {
		this.isopath = isopath;
		raf = new RandomAccessFile(this.isopath, "r");
		readSector(raf, 16);
	}

	public void readSector(RandomAccessFile raf, int nSector) throws Exception {
			raf.seek(SECTORSIZE * nSector);
			byte[] sector = new byte[SECTORSIZE];
			raf.read(sector);
			String header = getHeader(sector);
			if (!"CD001".equals(header)
					&& !"BEA01".equals(header)) {
				throw new Exception("Could not find DVD or BR header");
			}

			this.rootFS = createRootFolder(this.raf, sector);
	}
			
	private String getHeader(byte[] sector) {
		return new String(Arrays.copyOfRange(sector, 1, 6));
	}

	private ISO9660DiskImageFS createRootFolder(RandomAccessFile raf,
			byte[] data) {
		int startSector = ISOUtils.readLittleEndianWord(Arrays.copyOfRange(
				data, 158, 162));
		int size = ISOUtils.readLittleEndianWord(Arrays.copyOfRange(data, 166,
				170));
		return new ISO9660DiskImageFS(startSector, size, "", true, raf);
	}

	private boolean isHeader(byte[] header) {
		try {
			return new String(header, "ASCII").equals("CD001");
		} catch (UnsupportedEncodingException e) {
			// never happens on ascii
		}
		return false;
		/*
		return (header[1] == 67) 
			&& (header[2] == 68) 
			&& (header[3] == 48)
			&& (header[4] == 48)
			&& (header[5] == 49);
				*/
	}

	private boolean isPrimaryVolumeDescriptor(byte b) {
		return 1 == ISOUtils.UValue(b);
	}

	public boolean existsFile(String filePath) {
		return this.rootFS.existsFile(filePath);
	}

	public byte[] getFile(String filePath) {
		return this.rootFS.getFile(filePath);
	}

	public void close() {
		try {
			this.raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, ISO9660DiskImageFS> getFiles(){
		return (HashMap<String, ISO9660DiskImageFS>)rootFS.getFiles();
	}
}
