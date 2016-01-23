package ch.vorburger.minecraft.cursedl.tests;

import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import ch.vorburger.minecraft.cursedl.CurseManifest;
import ch.vorburger.minecraft.cursedl.Downloader;

public class CurseDownloaderTest {

	@Test
	public void testDownloadOneFile() throws Exception {
		new Downloader(Files.createTempDir()).download(223248, 2237600);
	}
	
	@Test @Ignore
	public void testCurseDownloader() throws Exception {
		URL url = Resources.getResource("manifest.json");
		String manifest = Resources.toString(url, Charsets.UTF_8);
		CurseManifest mf = CurseManifest.fromString(manifest);
		// System.out.println(mf);
		new Downloader(Files.createTempDir()).download(mf);
	}

}
