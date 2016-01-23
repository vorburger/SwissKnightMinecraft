package ch.vorburger.minecraft.cursedl;

import java.io.File;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("USAGE: CurseDownloader* manifest.json output-directory");
			System.exit(-1);
		}
		CurseManifest mf = CurseManifest.fromFile(new File(args[0]));
		new Downloader(new File(args[1])).download(mf);
	}

}
