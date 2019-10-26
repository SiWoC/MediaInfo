package nl.siwoc.mediainfo.iso;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;

public class ISO9660DiskImageFS {
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final int SECTORSIZE = 2048;
	private static String INTERESTING_FOLDERS = "VIDEO_TS";
	
	private boolean isDirectory;
	private RandomAccessFile raf;
	private int startSector;
	private int size;
	private String name;
	private HashMap<String, ISO9660DiskImageFS> files;

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public RandomAccessFile getRaf() {
		return raf;
	}

	public void setRaf(RandomAccessFile raf) {
		this.raf = raf;
	}

	public int getStartSector() {
		return startSector;
	}

	public void setStartSector(int startSector) {
		this.startSector = startSector;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, ISO9660DiskImageFS> getFiles() {
		return files;
	}

	private void addFile(String filename, ISO9660DiskImageFS file) {
		if (getFiles() == null) {
			this.files = new HashMap<String, ISO9660DiskImageFS>();
		}
		getFiles().put(filename, file);
	}

	public ISO9660DiskImageFS(int startSector, int size, String name, boolean isDirectory, RandomAccessFile raf) throws Exception {
		setStartSector(startSector);
		setSize(size);
		setName(name);
		setDirectory(isDirectory);
		setRaf(raf);
		if (isDirectory && (getName().equals("") || getName().matches(INTERESTING_FOLDERS)))
			seekFiles();
	}

	public void seekFiles() throws Exception {
		byte[] data = new byte[getSize()];
		getRaf().seek(SECTORSIZE * getStartSector());
		getRaf().read(data);
		int count = 0;
		for (int index = 0; index < data.length; index++) {
			int offset = data[index];
			if (offset == 0)
				break;
			if (count > 1) {
				parseFile(Arrays.copyOfRange(data, index, index + offset));
				index += offset - 1;
			} else {
				count++;
				index += offset - 1;
			}
		}
	}

	private void parseFile(byte[] data) throws Exception {
		int startSector = ISOUtils.readIntLE(data, 2);
		int size = ISOUtils.readIntLE(data, 10);
		boolean isDir = (ISOUtils.getSingleBit(data[25], 1) == 1);
		int filenameLength = ISOUtils.getUnsignedValue(data[32]);
		String filename;
		if (isDir) {
			filename = new String(Arrays.copyOfRange(data, 33, 33 + filenameLength));
		} else {
			filename = new String(Arrays.copyOfRange(data, 33, 31 + filenameLength));
		}

		addFile(filename, new ISO9660DiskImageFS(startSector, size, filename, isDir, this.raf));
	}

	public boolean existsFile(String filePath) {
		if (getFiles() == null) {
			return false;
		}
		if (getFiles().containsKey(filePath)) {
			return true;
		}
		if (filePath.contains(FILE_SEPARATOR)) {
			String folder = filePath.substring(0, filePath.indexOf(FILE_SEPARATOR));
			if (getFiles().containsKey(folder)) {
				return getFiles().get(folder).existsFile(filePath.substring(1 + filePath.indexOf(FILE_SEPARATOR)));
			}
		}
		return false;
	}

	public ISO9660DiskImageFS getFile(String filePath) throws IOException {
		if (getFiles() == null || (getFiles().containsKey(filePath) && getFiles().get(filePath).isDirectory())) {
			return null;
		}
		if (filePath.contains(FILE_SEPARATOR)) {
			String folder = filePath.substring(0, filePath.indexOf(FILE_SEPARATOR));
			if (getFiles().containsKey(folder)) {
				return this.files.get(folder).getFile(filePath.substring(1 + filePath.indexOf(FILE_SEPARATOR)));
			}
		} else if (getFiles().containsKey(filePath)) {
			return getFiles().get(filePath);
		}
		return null;
	}
	
	public byte[] getData() throws IOException {
		byte[] fileData = new byte[getSize()];
		getRaf().seek(getStartSector() * SECTORSIZE);
		getRaf().read(fileData);
		return fileData;
		
	}

}
