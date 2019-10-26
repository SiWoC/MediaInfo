package test.nl.siwoc.mediainfo;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Ignore;
import org.junit.jupiter.api.*;

import nl.siwoc.mediainfo.FileProber;
import nl.siwoc.mediainfo.MediaInfo;

class FileProberTests {

	static FileProber fp;
	
	@BeforeAll
	static void beforeAll() {
		System.out.println("Running setup");
		try {
			new File("log").mkdir();
			FileHandler handler = new FileHandler("log/FileProber.log", 500000, 2, true);
			handler.setFormatter(new SimpleFormatter());
			Logger.getLogger("").addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fp = new FileProber();
		FileProber.setLogLevel(Level.FINER);
	}


	@Test
	void testMKV_1() {
		String filename = "O:/downloads/Aladdin (1992)/Aladdin (1992).mkv";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("mkv", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg4", mediaInfo.getVideoCodec());
			Assertions.assertEquals(1920, mediaInfo.getFrameWidth());
			Assertions.assertEquals(1080, mediaInfo.getFrameHeight());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	@Test
	void testMKV_2() {
		//MKV 3D
		String filename = "O:/Kinder films/Frozen (2013) 3D/Disney's Frozen (2013) 3D [ID moviemeter 92952].mkv";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("mkv", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg4", mediaInfo.getVideoCodec());
			Assertions.assertEquals(1920, mediaInfo.getFrameWidth());
			Assertions.assertEquals(1080, mediaInfo.getFrameHeight());
			Assertions.assertEquals("dts", mediaInfo.getAudioCodec());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	@Test
	void testAVI_1() {
		String filename = "O:/downloads/Shazam (2019)/Shazam (2019).avi";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("avi", mediaInfo.getContainer());
			Assertions.assertEquals("h264", mediaInfo.getVideoCodec());
			Assertions.assertEquals(1280, mediaInfo.getFrameWidth());
			Assertions.assertEquals(536, mediaInfo.getFrameHeight());
			Assertions.assertEquals("nnwv", mediaInfo.getAudioCodec());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	@Test
	void testAVI_2() {
		String filename = "O:/Series/Red Dwarf/S01/Red Dwarf S01E01 - The End.avi";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("avi", mediaInfo.getContainer());
			Assertions.assertEquals("xvid", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(416, mediaInfo.getFrameHeight());
			Assertions.assertEquals("mp3", mediaInfo.getAudioCodec());
			Assertions.assertEquals(2, mediaInfo.getAudioChannels());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	@Test
	void testMP4_1() {
		//MP4
		String filename = "O:/Kinder films/Free Birds (2013)/Free Birds.mp4";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("mp4", mediaInfo.getContainer());
			Assertions.assertEquals("mp42", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(404, mediaInfo.getFrameHeight());
			Assertions.assertEquals(2, mediaInfo.getAudioChannels());
			Assertions.assertEquals("mp4a", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}
	
	@Test
	void testMOV_1() {
		//MP4
		String filename = "N:/Casper/huiswerk/Film NL/Dood.MOV";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("mov", mediaInfo.getContainer());
			Assertions.assertEquals("qt", mediaInfo.getVideoCodec());
			Assertions.assertEquals(640, mediaInfo.getFrameWidth());
			Assertions.assertEquals(480, mediaInfo.getFrameHeight());
			Assertions.assertEquals(1, mediaInfo.getAudioChannels());
			Assertions.assertEquals("sowt", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}
	
	@Test
	void testDVD_1() {
		//DVD Menu???, no audio
		String filename = "O:/Kinder films/G-Force (2009)/VIDEO_TS/VTS_01_0.IFO";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("dvd", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg2", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(480, mediaInfo.getFrameHeight());
			Assertions.assertEquals(0, mediaInfo.getAudioChannels());
			Assertions.assertEquals(null, mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	@Test
	void testDVD_2() {
		//DVD video + audio
		String filename = "O:/Kinder films/G-Force (2009)/VIDEO_TS/VTS_07_0.IFO";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("dvd", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg2", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(576, mediaInfo.getFrameHeight());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}
	
	@Test
	void testDVD_3() {
		//DVD video + audio
		String filename = "O:/Kinder films/G-Force (2009)/VIDEO_TS";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("dvd", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg2", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(576, mediaInfo.getFrameHeight());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}
	
	@Test
	void testDVD_4() {
		//DVD video + audio
		String filename = "O:/Kinder films/G-Force (2009)";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("dvd", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg2", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(576, mediaInfo.getFrameHeight());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}
	
	@Test
	void testISO_1() {
		//ISO zip readable
		String filename = "O:/Kinder films/Early Man (2018)/Early Man (2018).iso";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("dvdiso", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg2", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(576, mediaInfo.getFrameHeight());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	@Test
	void testISO_2() {
		//ISO BR with zip
		String filename = "O:/Kinder films/The Pirates Band of Misfits (2012)/The Pirates Band of Misfits (2012) 3D.iso";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("briso", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg2", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(576, mediaInfo.getFrameHeight());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	@Test
	void testISO_3() {
		//ISO BR not zip
		String filename = "O:/downloads/Hotel Transylvania 3 Summer Vacation (2018)/Hotel Transylvania 3 Summer Vacation (2018).iso";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("briso", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg2", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(480, mediaInfo.getFrameHeight());
			Assertions.assertEquals(0, mediaInfo.getAudioChannels());
			Assertions.assertEquals(null, mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	@Test
	void testISO_4() {
		//ISO DVD5
		String filename = "O:/Kinder films/Coco (2017)/Coco (2017).iso";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("dvdiso", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg2", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(576, mediaInfo.getFrameHeight());
			Assertions.assertEquals(2, mediaInfo.getAudioChannels());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}
	
	@Test
	void testISO_5() {
		//ISO DVD9
		String filename = "O:/Kinder films/De Lorax en het Verdwenen Bos (2012)/DR_SEUSS_THE_LORAX.iso";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("dvdiso", mediaInfo.getContainer());
			Assertions.assertEquals("mpeg2", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(576, mediaInfo.getFrameHeight());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	// BluRay
	// "O:/Films/Star Trek/Star Trek Beyond (2016)"
}
