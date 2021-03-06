package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ProcessUserData {
	ManifestInject manifestInject;
	private String rootDirectoryPath;
	private File rootDirectory;
	File manifest;
	boolean manifestFound;
	
	private Scanner scannerSTDIN;
	
	public ProcessUserData(Scanner scannerSTDIN) {
		this.scannerSTDIN = scannerSTDIN;
		manifestInject = new ManifestInject(this.scannerSTDIN);
		manifestFound = false;
	}
	
	public void processRootDirectory() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {
		scanPath();
		checkRoot();
		processScanResults();
	}
	
	public void scanPath() {
		System.out.println("Please enter the complete path to the root directory of your Android application");
		
		this.rootDirectoryPath = scannerSTDIN.nextLine();
	}
	
	public void checkRoot() {
		try {
			this.rootDirectoryPath = BriskNoteDir;
			
			rootDirectory = new File(rootDirectoryPath);
			
			if (rootDirectory.isDirectory()) {
				scanDir(rootDirectory);
			} else {
				System.out.println("The path provided does not point to a directory");
				Scanner scanner = new Scanner(rootDirectory);
				scanner.close();
				System.exit(0);
			}			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("The program experienced an error and exited.");
			System.exit(0);
		}
	}
	
	public void scanDir(File dir) {
		File[] listOfFiles = dir.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			File currFile = listOfFiles[i];
			
			if (currFile.isDirectory()) {
				scanDir(currFile);
			} else if (currFile.isFile() && 
					currFile.getName().equals("AndroidManifest.xml")) {
				manifestFound = true;
				manifest = currFile;
				return;
			}
		}
	}
	
	public void processScanResults() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {
		if (manifestFound) {
			manifestInject.processAndroidManifestXML(manifest);
		} else {
			manifestInject.manifestNotFound();
		}
	}
	
	public File getRootDirectory() {
		return this.rootDirectory;
	}

	public final static String duckduckgoDir = "/Users/stormfootball4life/Desktop/CS/Columbia/SoftwareSystemsLab/android/";
	public final static String BriskNoteDir = "/Users/stormfootball4life/AndroidStudioProjects/BriskNote/";
}