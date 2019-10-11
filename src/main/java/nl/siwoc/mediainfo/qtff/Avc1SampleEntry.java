package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Avc1SampleEntry extends SampleEntry {

	public Avc1SampleEntry(Box parent, int size, byte[] data) throws Exception {
		setType("avc1");
		setSize(size);
		setParent(parent);
		LOGGER.info("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	codingname{
		 unsigned int(16) pre_defined = 0;
		 const unsigned int(16) reserved = 0;
		 unsigned int(32)[3] pre_defined = 0;
		 unsigned int(16) width;
		 unsigned int(16) height;
		 template unsigned int(32) horizresolution = 0x00480000; // 72 dpi
		 template unsigned int(32) vertresolution = 0x00480000; // 72 dpi
		 const unsigned int(32) reserved = 0;
		 template unsigned int(16) frame_count = 1;
		 string[32] compressorname;
		 template unsigned int(16) depth = 0x0018;
		 int(16) pre_defined = -1;
		 */
}
