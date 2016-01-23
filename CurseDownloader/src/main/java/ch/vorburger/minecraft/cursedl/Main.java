package ch.vorburger.minecraft.cursedl;

import java.io.File;

public class Main {

	public static void main(String[] args) throws Exception {
		CurseManifest mf = CurseManifest.fromFile(new File(args[0]));

	}

}
