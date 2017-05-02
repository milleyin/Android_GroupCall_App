package com.afmobi.palmchat.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.content.Context;

import com.afmobi.palmchat.log.PalmchatLogUtils;

/**
 * @author gtf
 */
public class ZipFileUtils {

	private static ZipFileUtils zipFileUtils;

	public ZipFileUtils() {
	}

	public static ZipFileUtils getInstance() {
		if (zipFileUtils == null) {
			zipFileUtils = new ZipFileUtils();
		}
		return zipFileUtils;

	}

	/**
	 * Decompression functions would zipFile extract the files to the folderPath
	 * directory.
	 * 
	 * @throws Exception
	 */
	public int upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
		/* add try catch语句块 by zhh */
		// public static void upZipFile() throws Exception{
		try {
			ZipFile zfile = new ZipFile(zipFile);
			Enumeration zList = zfile.entries();
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			while (zList.hasMoreElements()) {
				ze = (ZipEntry) zList.nextElement();
				if (ze.isDirectory()) {
					PalmchatLogUtils.d("upZipFile", "ze.getName() = " + ze.getName());
					String dirstr = folderPath + ze.getName();
					// dirstr.trim();
					dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
					PalmchatLogUtils.d("upZipFile", "str = " + dirstr);
					File f = new File(dirstr);
					f.mkdir();
					continue;
				}
				PalmchatLogUtils.d("upZipFile", "ze.getName() = " + ze.getName());
				OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
				InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
				int readLen = 0;
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					os.write(buf, 0, readLen);
				}
				is.close();
				os.close();
			}
			zfile.close();
			PalmchatLogUtils.d("upZipFile", "finishssssssssssssssssssss");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Given the root directory, returns a relative path corresponding to the
	 * actual file name.
	 * 
	 * @ Param baseDir Specify the root directory @ Param absFileName Relative
	 * path name, the name comes from the ZipEntry @ Return java.io.File actual
	 * file
	 */
	public File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		File ret = new File(baseDir);
		String substr = null;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				substr = dirs[i];
				try {
					// substr.trim();
					substr = new String(substr.getBytes("8859_1"), "GB2312");

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = new File(ret, substr);

			}
			PalmchatLogUtils.d("upZipFile", "1ret = " + ret);
			if (!ret.exists())
				ret.mkdirs();
			substr = dirs[dirs.length - 1];
			try {
				// substr.trim();
				substr = new String(substr.getBytes("8859_1"), "GB2312");
				PalmchatLogUtils.d("upZipFile", "substr = " + substr);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ret = new File(ret, substr);
			PalmchatLogUtils.d("upZipFile", "2ret = " + ret);
			return ret;
		}
		return ret;
	}

}
