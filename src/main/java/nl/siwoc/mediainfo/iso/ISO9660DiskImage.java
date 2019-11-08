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
package nl.siwoc.mediainfo.iso;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.logging.Logger;

import nl.siwoc.mediainfo.utils.ReadUtils;

/* 
 * based on https://github.com/zevektor/DiskImageReader/graphs/contributors
 * by Alessandro Mangone (Vektor) zevektor
 */
public class ISO9660DiskImage {

	private static final Logger LOGGER = Logger.getLogger(ISO9660DiskImage.class.getName());
	
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
		int startSector = ReadUtils.readInt32LE(data, 158);
		LOGGER.finer("StartSector: " + startSector);
		int size = ReadUtils.readInt32LE(data, 166);
		LOGGER.finer("Size: " + size);
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
