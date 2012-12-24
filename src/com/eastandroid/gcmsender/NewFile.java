package com.eastandroid.gcmsender;

import java.io.File;
import java.net.URI;

public class NewFile extends File {
	private static final long serialVersionUID = -1606766234390316858L;

	public NewFile(File dir, String name) {
		super(dir, name);
	}

	public NewFile(String dirPath, String name) {
		super(dirPath, name);
	}

	public NewFile(String path) {
		super(path);
	}

	public NewFile(URI uri) {
		super(uri);
	}

}
