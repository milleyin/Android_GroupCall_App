package com.afmobi.palmcall.service;

import com.afmobi.palmcall.innerApi.api.SourceDownloadApi;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;

@Repository
public class SourceDownloadService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private SourceDownloadApi sourceDownloadApi;

	@Resource
	private String uploadCoverImgPath;

	@Async("apiExecutor")
	public String downloadMidAvatar(String localImgPath) {
		String[] source = fixLocalImgPath(localImgPath);
		if(source == null) {
			return null;
		}

		String afid = source[0];
		String sn = source[1];

		String imgName = afid + ".jpg";
		String pixel = "160x160";
		
		return downloadSource(afid, sn, imgName, pixel);
	}

	private String downloadSource(String afid, String sn, String imgName, String pixel) {
		File imgSource = new File(uploadCoverImgPath + "/" + imgName);

		log.debug("资源在本地(" + imgSource.getAbsolutePath() + ")是否存在：" + imgSource.exists());

		if(imgSource.exists()) {
			return imgName;
		} else {
			try {
				Map<String, Object> result = sourceDownloadApi.sent(afid, pixel, sn);
				byte[] fileByte = null;
				fileByte = (byte[]) result.get("content");

				if (fileByte == null || fileByte.length == 0) {
					return null;
				}

				downloadSourceToLocal(imgName, fileByte);
			} catch (Exception e) {
				return null;
			}

			return imgName;
		}
	}

	private void downloadSourceToLocal(String url, byte[] fileByte) throws Exception {
		log.debug("写入资源：" + url);
		log.debug("内容字节数：" + fileByte.length);

		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileByte);){
			BufferedImage src = ImageIO.read(byteArrayInputStream);
			File staticFile = new File(uploadCoverImgPath + "/" + url);

			ImageIO.write(src, "jpg", staticFile);
		} catch (Exception e) {
			throw e;
		}

	}
	
	private String[] fixLocalImgPath(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}

		String[] strs = source.split(",");
		if (strs == null || strs.length < 2) {
			return null;
		}

		return strs;
	}

}
