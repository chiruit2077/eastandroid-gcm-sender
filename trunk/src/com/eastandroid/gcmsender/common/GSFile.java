package com.eastandroid.gcmsender.common;

import android.common.BaseFile;

/**
 * <pre>
 * 기능 : ssp관련 파일IO Util
 * 
 *  
 *  file     => xxxxxxxxxx.xxx              filename and ext
 *  filename => xxxxxxxxxxx                 has no ext
 *  pathfile => /xxxx/xxxxx/xxxxxxxxx.xxx   absolute path + filename + ext
 *  ext      => .xxx                        note : has dot(".")
 * </pre>
 */

public class GSFile extends BaseFile {

	public enum EXT {
		config, message, targets, ;

		public String s() {

			return name();
		}

	}

	private static final String LAST_TARGET = "autosave_target";
	private static final String LAST_CONFIG = "autosave_config";
	private static final String LAST_MESSAGE = "autosave_message";
	public static String getLastTartet() {
		return getVPathfile(GSFile.LAST_TARGET, GSFile.EXT.targets.s()).getAbsolutePath();
	}
	public static String getLastMessage() {
		return getVPathfile(GSFile.LAST_MESSAGE, GSFile.EXT.message.s()).getAbsolutePath();
	}
	public static String getLastConfig() {
		return getVPathfile(GSFile.LAST_CONFIG, GSFile.EXT.config.s()).getAbsolutePath();
	}
}
