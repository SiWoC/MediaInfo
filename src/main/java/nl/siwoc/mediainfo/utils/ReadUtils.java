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
package nl.siwoc.mediainfo.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ReadUtils {

	private static double[] WEIGHT = {
			1D,						// 16^0
			256D,					// 16^2
			65536D,					// 16^4
			16777216D,				// 16^6
			4294967296D,			// 16^8
			1099511627776D,			// 16^10
			281474976710656D,		// 16^12
			72057594037927936D};	// 16^14
	
	/**
	 * Reads a Four Character Code from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return A Four Character Code read from the stream
	 * @throws Exception when we can not read 4 bytes of the stream
	 */
	public static String readFourCC(InputStream is) throws Exception {
		byte[] bytes = new byte[4];
		if (is.read(bytes) != 4) throw new Exception("Bytes read != 4, could not readFourCC");
		return new String(bytes, "ASCII");
	}

	/**
	 * Returns the Unsigned int value of a byte
	 * @param b the byte
	 * @return a short containing the Unsigned value
	 */
	public static short getUnsignedValue(byte b) {
		return (short) (b & 0xFF);
	}

	/**
	 * Read a Signed int16 BIG_ENDIAN from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return A Signed int16 read BIG_ENDIAN from the stream
	 * @throws Exception when we can not read 4 bytes of the stream
	 */
	public static short readInt16BE(InputStream is) throws Exception {
		byte[] bytes = new byte[2];
		if (is.read(bytes) != 2) throw new Exception("Bytes read != 2, could not readShortBE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort();
	}

	/**
	 * Read a Signed int16 LITTLE_ENDIAN from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return A Signed int16 read LITTLE_ENDIAN from the stream
	 * @throws Exception when we can not read 4 bytes of the stream
	 */
	public static short readInt16LE(InputStream is) throws Exception {
		byte[] bytes = new byte[2];
		if (is.read(bytes) != 2) throw new Exception("Bytes read != 2, could not readShortLE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	/**
	 * Read a Signed int32 BIG_ENDIAN from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return A Signed int32 read BIG_ENDIAN from the stream
	 * @throws Exception when we can not read 4 bytes of the stream
	 */
	public static int readInt32BE(InputStream is) throws Exception {
		byte[] bytes = new byte[4];
		if (is.read(bytes) != 4) throw new Exception("Bytes read != 4, could not readIntBE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
	}

	/**
	 * Read a Signed int32 LITTLE_ENDIAN from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return A Signed int32 read LITTLE_ENDIAN from the stream
	 * @throws Exception when we can not read 4 bytes of the stream
	 */
	public static int readInt32LE(InputStream is) throws Exception {
		byte[] bytes = new byte[4];
		if (is.read(bytes) != 4) throw new Exception("Bytes read != 4, could not readIntLE");
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	/**
	 * Returns the Signed int32 LITTLE_ENDIAN value of a set of 4 bytes in a byte-array starting at offset
	 * @param data the byte array from which to read
	 * @param offset the offset from which to start
	 * @return the int32 LITTLE_ENDIAN value of the set of 4 bytes
	 * @throws Exception when there are not enough bytes starting from offset
	 */
	public static int readInt32LE(byte[] data, int offset) throws Exception {
		if (data.length < offset + 4) throw new Exception("data too short [" + data.length + "] for offset + 4 [" + offset + "], could not readIntLE");
		byte[] bytes = Arrays.copyOfRange(data, offset, offset + 4);
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	/**
	 * Read an Unsigned int16 BIG_ENDIAN from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return An Unsigned int16 (as int) read BIG_ENDIAN from the stream
	 * @throws Exception when we can not read 2 bytes of the stream
	 */
	public static int readUInt16BE(InputStream is) throws Exception {
		int result = 0;

		for (int i = 0; i < 2; i++) {
			// is.read reads an UNSIGNED int
			result += is.read() * WEIGHT[1 - i];
		}
		return result;	
	}

	/**
	 * Read an Unsigned int32 BIG_ENDIAN from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return An Unsigned int32 (as long) read BIG_ENDIAN from the stream
	 * @throws Exception when we can not read 4 bytes of the stream
	 */
	public static long readUInt32BE(InputStream is) throws Exception {
		long result = 0;

		for (int i = 0; i < 4; i++) {
			// is.read reads an UNSIGNED int
			result += is.read() * WEIGHT[3 - i];
		}
		return result;	
	}

	/**
	 * Read an Unsigned int64 BIG_ENDIAN from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return An Unsigned int64 (as BigDecimal) read BIG_ENDIAN from the stream
	 * @throws Exception when we can not read 8 bytes of the stream
	 */
	public static BigDecimal readUInt64BE(InputStream is) throws Exception {
		BigDecimal result = BigDecimal.ZERO;

		for (int i = 0; i < 8; i++) {
			// is.read reads an UNSIGNED int
			result = result.add(new BigDecimal(is.read() * WEIGHT[7 - i]));
		}
		return result;	
	}

	/**
	 * Read an Unsigned int32 LITTLE_ENDIAN from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return An Unsigned int32 (as long) read LITTLE_ENDIAN from the stream
	 * @throws Exception when we can not read 4 bytes of the stream
	 */
	public static long readUInt32LE(InputStream is) throws Exception {
		long result = 0;

		for (int i = 0; i < 4; i++) {
			// is.read reads an UNSIGNED int
			result += is.read() * WEIGHT[i];
		}
		return result;	
	}

	/**
	 * Read an array of 3 bytes (flag) from an InputStream
	 * @param is the InputStream to read bytes from
	 * @return An array of 3 bytes read from the stream
	 * @throws Exception when we can not read 3 bytes of the stream
	 */
	public static byte[] readFlag(InputStream is) throws Exception {
		byte[] bytes = new byte[3];
		if (is.read(bytes) != 3) throw new Exception("Bytes read != 3, could not readFlag");
		return bytes;
	}

	/**
	 * Returns the value of a bit in a byte <br>
	 * For example for a byte value of 9 = 00001001 <br>
	 * running this for position 0 or 3 returns 1 <br>
	 * @param b the byte to read from 
	 * @param position the 0-based position of the bit you want to know
	 * @return The value of the bit 1 or 0
	 */
	public static int getSingleBit(int b, int position)
	{
	   return (b >> position) & 1;
	}

	/**
	 * Returns the combined value of 2 bits (position and position + 1) in a byte <br>
	 * For example for a byte value of 9 = 00001001 <br>
	 * running this for position 0 returns bits 01 = decimal 1 <br>
	 * running this for position 1 returns bits 00 = decimal 0 <br>
	 * running this for position 2 returns bits 10 = decimal 2 <br>
	 * @param b the byte to read from 
	 * @param position the 0-based position of the bit you want to know
	 * @return The combined value of the bits (position and position + 1)
	 */
	public static int getTwoBits(int b, int position)
	{
	   return (b >> position) & 3;
	}

	/**
	 * Returns the combined value of 3 bits (position, position + 1 and position + 2) in a byte <br>
	 * For example for a byte value of 9 = 00001001 <br>
	 * running this for position 0 returns bits 001 = decimal 1 <br>
	 * running this for position 1 returns bits 100 = decimal 4 <br>
	 * running this for position 2 returns bits 010 = decimal 2 <br>
	 * @param b the byte to read from 
	 * @param position the 0-based position of the bit you want to know
	 * @return The combined value of the bits (position and position + 1)
	 */
	public static int getThreeBits(int b, int position)
	{
	   return (b >> position) & 7;
	}

	public static void main(String[] args) {
		System.out.println(getThreeBits(9, 0));
		System.out.println(getThreeBits(9, 1));
		System.out.println(getThreeBits(9, 2));
		String filename = "O:/Films/Glass (2019)/Glass (2019).mp4";
		try (FileInputStream fis = new FileInputStream(filename)) {
			fis.skip(36);
			System.out.println(readFourCC(fis));
	        System.out.println(readUInt32BE(fis));
	        fis.skip(139);
	        System.out.println(readUInt64BE(fis));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
