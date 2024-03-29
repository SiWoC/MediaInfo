package test.nl.siwoc.mediainfo;

import org.junit.jupiter.api.*;

import nl.siwoc.mediainfo.FileProber;
import nl.siwoc.mediainfo.MediaInfo;

class FileProberTests {

	static FileProber fp;
	
	@BeforeAll
	static void beforeAll() {
		System.out.println("Running setup");
		fp = new FileProber();
	}


	@Test
	void testMKV_1() {
		String filename = "O:/downloads/Aladdin (1992)/Aladdin (1992).mkv";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("mkv", mediaInfo.getContainer());
			Assertions.assertEquals("xvid", mediaInfo.getVideoCodec());
			Assertions.assertEquals(1904, mediaInfo.getFrameWidth());
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
			Assertions.assertEquals("h264", mediaInfo.getVideoCodec());
			Assertions.assertEquals(1920, mediaInfo.getFrameWidth());
			Assertions.assertEquals(1080, mediaInfo.getFrameHeight());
			Assertions.assertEquals("dts", mediaInfo.getAudioCodec());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}
	
	@Test
	void testMKV_3() {
		//MKV
		String filename = "O:/downloads/Wallace and Gromit - A Matter of Loaf and Death (2008)/Wallace and Gromit - A Matter of Loaf and Death (2008).mkv";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("mkv", mediaInfo.getContainer());
			Assertions.assertEquals("h264", mediaInfo.getVideoCodec());
			Assertions.assertEquals(1920, mediaInfo.getFrameWidth());
			Assertions.assertEquals(1080, mediaInfo.getFrameHeight());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}
	
	@Test
	void testMKV_4() {
		//MKV
		String filename = "O:/Films/Pacific Rim Uprising (2018)/Pacific Rim Uprising (2018).mkv";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("mkv", mediaInfo.getContainer());
			Assertions.assertEquals("h264", mediaInfo.getVideoCodec());
			Assertions.assertEquals(1280, mediaInfo.getFrameWidth());
			Assertions.assertEquals(536, mediaInfo.getFrameHeight());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}
	
	// "O:/downloads/The.Curse.of.Oak.Island.S09E04.Spoils.Alert.720p.HEVC.x265-MeGusta/The.Curse.of.Oak.Island.S09E04.Spoils.Alert.720p.HEVC.x265-MeGusta.mkv"
	@Test
	void testMKV_5() {
		//MKV
		String filename = "O:/downloads/The.Curse.of.Oak.Island.S09E04.Spoils.Alert.720p.HEVC.x265-MeGusta/The.Curse.of.Oak.Island.S09E04.Spoils.Alert.720p.HEVC.x265-MeGusta.mkv";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("mkv", mediaInfo.getContainer());
			Assertions.assertEquals("h265", mediaInfo.getVideoCodec());
			Assertions.assertEquals(1280, mediaInfo.getFrameWidth());
			Assertions.assertEquals(720, mediaInfo.getFrameHeight());
			Assertions.assertEquals("aac", mediaInfo.getAudioCodec());
			Assertions.assertEquals(2, mediaInfo.getAudioChannels());
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
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
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
			Assertions.assertEquals(2, mediaInfo.getAudioChannels());
			Assertions.assertEquals("mp3", mediaInfo.getAudioCodec());
		} else {
			Assertions.fail("No MediaInfo returned");
		}
	}

	@Test
	void testAVI_3() {
		String filename = "O:/Films/Spider Man Into the Spider-Verse (2018)/Into the Spider-Verse (2018) [ID MOVIEMETER 1119894].avi";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("avi", mediaInfo.getContainer());
			Assertions.assertEquals("xvid", mediaInfo.getVideoCodec());
			Assertions.assertEquals(720, mediaInfo.getFrameWidth());
			Assertions.assertEquals(304, mediaInfo.getFrameHeight());
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
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
	void testMP4_2() {
		//MP4
		String filename = "O:/Films/Glass (2019)/Glass (2019).mp4";
		MediaInfo mediaInfo = fp.getMediaInfo(filename);
		if (mediaInfo != null) {
			Assertions.assertEquals("mp4", mediaInfo.getContainer());
			Assertions.assertEquals("isom", mediaInfo.getVideoCodec());
			Assertions.assertEquals(1280, mediaInfo.getFrameWidth());
			Assertions.assertEquals(720, mediaInfo.getFrameHeight());
			Assertions.assertEquals(2, mediaInfo.getAudioChannels());
			Assertions.assertEquals("ac3", mediaInfo.getAudioCodec());
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
			Assertions.assertEquals(6, mediaInfo.getAudioChannels());
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
	
	// O:/Kinder films/Jasper & Julia en de Dappere Ridders/Jasper & Julia en de Dappere Ridders.iso
	@Test
	void testISO_6() {
		String filename = "O:/Kinder films/Jasper & Julia en de Dappere Ridders/Jasper & Julia en de Dappere Ridders.iso";
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
