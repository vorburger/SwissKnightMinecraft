package ch.vorburger.minecraft.cursedl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;

import ch.vorburger.minecraft.cursedl.CurseManifest.CurseManifestFile;

/**
 * Download files from Curse.com.
 *  
 * @author Michael Vorburger.ch
 */
public class Downloader {

	protected HttpTransport http = new ApacheHttpTransport();
	protected HttpRequestFactory requestFactory = http.createRequestFactory();
	
	protected File dir;

	public Downloader(File directory) {
		this.dir = directory;
	}

	public void download(CurseManifest mf) throws IOException {
		int i = 1;
		log("Downloading " + mf.files.size() + " mods into " + dir + " ...");
		for (CurseManifestFile file : mf.files) {
			// TODO Parallelize!
			log(i + ". downloading projectID: " + file.projectID + ", fileID: " + file.fileID + " ..");
			download(file.projectID, file.fileID);
			++i;
		}
	}

	public void download(int projectID, int fileID) throws IOException {
		GenericUrl projectURL = new GenericUrl("http://minecraft.curseforge.com/projects/" + projectID);
		HttpRequest projectRequest = requestFactory.buildGetRequest(projectURL);
		// This will redirect - the point of this is just to obtain the new URL!
		HttpResponse projectResponse = projectRequest.execute();
		projectResponse.disconnect();

		GenericUrl newURL = projectResponse.getRequest().getUrl();
		newURL.appendRawPath("/files/" + fileID + "/download");
		try {
			download(newURL);
		} catch (HttpResponseException e) {
			// TODO Return this, instead of only logging
			log("FAILED to download "+ newURL + "; you should manually download another version of this mod from " + projectURL);
		}
	}

	protected void download(GenericUrl newURL) throws IOException {
		HttpRequest downloadRequest = requestFactory.buildGetRequest(newURL);
		HttpResponse response = null;
		try {
			response = downloadRequest.execute();
			
			List<String> pathParts = response.getRequest().getUrl().getPathParts();
			String fileName = pathParts.get(pathParts.size() - 1); // TODO tail
			File dlFile = new File(dir, fileName);
			try (OutputStream outputStream = new FileOutputStream(dlFile)) {
				response.download(outputStream);
			}
			log("Successfully downloaded: " + dlFile.getName());

		} finally {
			if (response != null)
				response.disconnect();
		}
	}

	protected void log(String msg) {
		System.out.println(msg);
	}

}
