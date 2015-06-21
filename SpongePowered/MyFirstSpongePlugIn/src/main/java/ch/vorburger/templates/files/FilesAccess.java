package ch.vorburger.templates.files;

public interface FilesAccess {

	// TODO support streams? Only if Xtend templates can produce them..
	
	void writeFile(String name, String content);
}
