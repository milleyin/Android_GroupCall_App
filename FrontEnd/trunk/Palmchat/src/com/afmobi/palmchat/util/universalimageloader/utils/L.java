/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.afmobi.palmchat.util.universalimageloader.utils;

import android.util.Log;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.universalimageloader.core.ImageLoader;

/**
 * "Less-word" analog of Android {@link Log logger}
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.6.4
 */
public final class L {

	private static final String LOG_FORMAT = "%1$s\n%2$s";
	private static volatile boolean DISABLED = false;

	private L() {
	}

	/** Enables logger (if {@link #disableLogging()} was called before) */
	public static void enableLogging() {
		DISABLED = false;
	}

	/** Disables logger, no logs will be passed to LogCat, all log methods will do nothing */
	public static void disableLogging() {
		DISABLED = true;
	}

	public static void d(String message, Object... args) {
		log(Log.DEBUG, null, message, args);
	}

	public static void i(String message, Object... args) {
		log(Log.INFO, null, message, args);
	}

	public static void w(String message, Object... args) {
		log(Log.WARN, null, message, args);
	}

	public static void e(Throwable ex) {
		log(Log.ERROR, ex, null);
	}

	public static void e(String message, Object... args) {
		log(Log.ERROR, null, message, args);
	}

	public static void e(Throwable ex, String message, Object... args) {
		log(Log.ERROR, ex, message, args);
	}

	private static void log(int priority, Throwable ex, String message, Object... args) {
		if (DISABLED||!PalmchatLogUtils.DEBUG) return;
		if (args.length > 0) {
			message = String.format(message, args);
		}

		String log;
		if (ex == null) {
			log = message;
		} else {
			String logMessage = message == null ? ex.getMessage() : message;
			String logBody = Log.getStackTraceString(ex);
			log = String.format(LOG_FORMAT, logMessage, logBody);
		}
//		Log.println(priority, ImageLoader.TAG, log);
		if(log.contains("Unable to resolve host")){
			if(ex!=null){
				ex.printStackTrace();
			}

		}
		PalmchatLogUtils.e(ImageLoader.TAG,log);
	}
}