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
package com.afmobi.palmchat.util.universalimageloader.cache.disc;

import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.util.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.afmobi.palmchat.util.universalimageloader.core.DefaultConfigurationFactory;

import java.io.File;

/**
 * Base disc cache. Implements common functionality for disc cache.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see DiscCacheAware
 * @see FileNameGenerator
 * @since 1.0.0
 */
public abstract class BaseDiscCache implements DiscCacheAware {

	private static final String ERROR_ARG_NULL = "\"%s\" argument must be not null";

	protected File cacheDir;

	private FileNameGenerator fileNameGenerator;

	public BaseDiscCache(File cacheDir) {
		this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator());
	}

	public BaseDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
		if (cacheDir == null) {
			throw new IllegalArgumentException(String.format(ERROR_ARG_NULL, "cacheDir"));
		}
		if (fileNameGenerator == null) {
			throw new IllegalArgumentException(String.format(ERROR_ARG_NULL, "fileNameGenerator"));
		}

		this.cacheDir = cacheDir;
		this.fileNameGenerator = fileNameGenerator;
	}

	@Override
	public File get(String key) {
		String fileName = fileNameGenerator.generate(key);//http://54.246.144.245:34588/d/afid=a15443301&pixel=160x160
														 //http://54.194.14.28:80/images/2015/10/6/W768_H768/73176002105111ad94ea4edca3fc4df3.jpg
		return new File(cacheDir, fileName);
	}

	@Override
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}
		}
	}
}