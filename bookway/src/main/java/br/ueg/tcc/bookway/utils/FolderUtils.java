package br.ueg.tcc.bookway.utils;

import java.io.File;

import br.com.vexillum.control.manager.ExceptionManager;

/**
 * @author Pedro
 * 
 */
public class FolderUtils {

	public static void createFolder(String path, String nameFolder) {
		String separator = System.getProperty("file.separator");
		String directoryName = path + separator + nameFolder;
		try {
			if (!new File(directoryName).exists())
				(new File(directoryName)).mkdir();
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
	}

	public static void deleteFolder(String directoryName) {
		if (new File(directoryName).exists())
			(new File(directoryName)).delete();
	}
}
