package nl.siwoc.mediainfo.riff.avi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.siwoc.mediainfo.riff.RIFFUtils;

public class BitmapInfo extends StreamFormat {

	private static final Logger LOGGER = Logger.getLogger(BitmapInfo.class.getName());
	
	private int width;
	private int height;
	private short planes;
	private short bitCount;
	private String compression;
	private int sizeImage;
	private int xPixelsPerMeter;
	private int yPixelsPerMeter;
	private int numberOfColorIndexesUsed;
	private int numberOfColorIndexesImportant;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public short getPlanes() {
		return planes;
	}

	public void setPlanes(short planes) {
		this.planes = planes;
	}

	public short getBitCount() {
		return bitCount;
	}

	public void setBitCount(short bitCount) {
		this.bitCount = bitCount;
	}

	public String getCompression() {
		return compression;
	}

	public void setCompression(String compression) {
		this.compression = compression;
	}

	public int getSizeImage() {
		return sizeImage;
	}

	public void setSizeImage(int sizeImage) {
		this.sizeImage = sizeImage;
	}

	public int getxPixelsPerMeter() {
		return xPixelsPerMeter;
	}

	public void setxPixelsPerMeter(int xPixelsPerMeter) {
		this.xPixelsPerMeter = xPixelsPerMeter;
	}

	public int getyPixelsPerMeter() {
		return yPixelsPerMeter;
	}

	public void setyPixelsPerMeter(int yPixelsPerMeter) {
		this.yPixelsPerMeter = yPixelsPerMeter;
	}

	public int getNumberOfColorIndexesUsed() {
		return numberOfColorIndexesUsed;
	}

	public void setNumberOfColorIndexesUsed(int numberOfColorIndexesUsed) {
		this.numberOfColorIndexesUsed = numberOfColorIndexesUsed;
	}

	public int getNumberOfColorIndexesImportant() {
		return numberOfColorIndexesImportant;
	}

	public void setNumberOfColorIndexesImportant(int numberOfColorIndexesImportant) {
		this.numberOfColorIndexesImportant = numberOfColorIndexesImportant;
	}

	public BitmapInfo(int size, byte[] data) throws Exception {
		setId("strf");
		setSize(size);
		try (InputStream is = new ByteArrayInputStream(data)){
			// read size again ?!?
			RIFFUtils.readIntLE(is);
			setWidth(RIFFUtils.readIntLE(is));
			setHeight(RIFFUtils.readIntLE(is));
			setPlanes(RIFFUtils.readShortLE(is));
			setBitCount(RIFFUtils.readShortLE(is));
			setCompression(RIFFUtils.readFourCC(is));
			setSizeImage(RIFFUtils.readIntLE(is));
			setxPixelsPerMeter(RIFFUtils.readIntLE(is));
			setyPixelsPerMeter(RIFFUtils.readIntLE(is));
			setNumberOfColorIndexesUsed(RIFFUtils.readIntLE(is));
			setNumberOfColorIndexesImportant(RIFFUtils.readIntLE(is));

			LOGGER.log(Level.FINE,"AVI BitmapInfo (strf): " + System.lineSeparator() +
				"   size=" + size + System.lineSeparator() +
				"   width=" + width + System.lineSeparator() +
				"   height=" + height + System.lineSeparator() +
				"   planes=" + planes + System.lineSeparator() +
				"   bitCount=" + bitCount + System.lineSeparator() +
				"   compression=[" + compression + "]" + System.lineSeparator() +
				"   sizeImage=" + sizeImage + System.lineSeparator() +
				"   xPixelsPerMeter=" + xPixelsPerMeter + System.lineSeparator() +
				"   yPixelsPerMeter=" + yPixelsPerMeter + System.lineSeparator() +
				"   numberOfColorIndexesUsed=" + numberOfColorIndexesUsed + System.lineSeparator() +
				"   numberOfColorIndexesImportant=" + numberOfColorIndexesImportant);

			/*
			int allowedBitCount = 0;
			boolean readPalette = false;
			switch (compression) {
			case NO_COMPRESSION:
			case NO_COMPRESSION_RGB:
			case NO_COMPRESSION_RAW:
				dataCompression = NO_COMPRESSION;
				dataTopDown = height<0;   //RGB mode is usually bottom-up, negative height signals top-down
				allowedBitCount = 8 | BITMASK24 | 32; //we don't support 1, 2 and 4 byte data
				readPalette = bitCount <= 8;
				break;
			case NO_COMPRESSION_Y8:
			case NO_COMPRESSION_GREY:
			case NO_COMPRESSION_Y800:
				dataTopDown = true;
				dataCompression = NO_COMPRESSION;
				allowedBitCount = 8;
				break;
			case NO_COMPRESSION_Y16:
			case NO_COMPRESSION_MIL:
				dataCompression = NO_COMPRESSION;
				allowedBitCount = 16;
				break;
			case AYUV_COMPRESSION:
				dataCompression = AYUV_COMPRESSION;
				allowedBitCount = 32;
				break;
			case UYVY_COMPRESSION:
			case UYNV_COMPRESSION:
				dataTopDown = true;
			case CYUV_COMPRESSION:  //same, not top-down
			case V422_COMPRESSION:
				dataCompression = UYVY_COMPRESSION;
				allowedBitCount = 16;
				break;
			case YUY2_COMPRESSION:
			case YUNV_COMPRESSION:
			case YUYV_COMPRESSION:
				dataTopDown = true;
				dataCompression = YUY2_COMPRESSION;
				allowedBitCount = 16;
				break;
			case YVYU_COMPRESSION:
				dataTopDown = true;
				dataCompression = YVYU_COMPRESSION;
				allowedBitCount = 16;
				break;
			case JPEG_COMPRESSION:
			case JPEG_COMPRESSION2:
			case JPEG_COMPRESSION3:
			case MJPG_COMPRESSION:
				dataCompression = JPEG_COMPRESSION;
				break;
			case PNG_COMPRESSION:
			case PNG_COMPRESSION2:
			case PNG_COMPRESSION3:
				dataCompression = PNG_COMPRESSION;
				break;
			default:
				throw new Exception("Unsupported compression: "+Integer.toHexString(compression)+
						(compression>=0x20202020 ? " '" + fourccString(compression)+"'" : ""));
			}

			int bitCountTest = bitCount==24 ? BITMASK24 : bitCount;  //convert "24" to a flag
			if (allowedBitCount!=0 && (bitCountTest & allowedBitCount)==0)
				throw new Exception("Unsupported: "+bitCount+" bits/pixel for compression '"+
						fourccString(compression)+"'");

			if (height < 0)       //negative height was for top-down data in RGB mode
				height = -height;

			// Scan line is padded with zeroes to be a multiple of four bytes
			scanLineSize = ((width * bitCount + 31) / 32) * 4;

			// a value of numberOfColorIndexesUsed=0 means we determine this based on the bits per pixel
			if (readPalette && numberOfColorIndexesUsed==0)
				numberOfColorIndexesUsed = 1 << bitCount;

			{
				System.out.println("   > data compression=0x" + Integer.toHexString(dataCompression)+" '"+fourccString(dataCompression)+"'");
				System.out.println("   > palette colors=" + numberOfColorIndexesUsed);
				System.out.println("   > scan line size=" + scanLineSize);
				System.out.println("   > data top down=" + dataTopDown);
			}

			//read color palette
			if (readPalette) {
				long spaceForPalette  = endPosition-raFile.getFilePointer();

				System.out.println("   Reading "+numberOfColorIndexesUsed+" Palette colors: " + posSizeString(spaceForPalette));
				if (spaceForPalette < numberOfColorIndexesUsed*4)
					throw new Exception("Not enough data ("+spaceForPalette+") for palette of size "+(numberOfColorIndexesUsed*4));
				byte[]  pr    = new byte[numberOfColorIndexesUsed];
				byte[]  pg    = new byte[numberOfColorIndexesUsed];
				byte[]  pb    = new byte[numberOfColorIndexesUsed];
				for (int i = 0; i < numberOfColorIndexesUsed; i++) {
					pb[i] = raFile.readByte();
					pg[i] = raFile.readByte();
					pr[i] = raFile.readByte();
					raFile.readByte();
				}
				new IndexColorModel(bitCount, numberOfColorIndexesUsed, pr, pg, pb);
			}
			*/
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	protected String getHandler() {
		return getCompression();
	}

	@Override
	protected short getChannels() {
		return 0;
	}

}
