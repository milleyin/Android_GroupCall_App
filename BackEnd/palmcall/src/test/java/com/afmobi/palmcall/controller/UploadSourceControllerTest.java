package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.outerApi.response.UploadSourceResponse;
import com.afmobi.palmcall.util.MockMultipartFileUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.HashMap;
import java.util.Map;

public class UploadSourceControllerTest extends CommonControllerTest {

	//----------------------------------上传封面图片--------------------------------------
	@Test
	public void test上传封面参数全为空() throws Exception {
		String json = requestContentString("/uploadSource", new HashMap<>());
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传封面pctoken为空() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传封面type为空() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传封面type参数错误() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		params.put("type", "esfs");
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传封面file为空() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		params.put("type", "1");
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传封面图片错误() throws Exception {
		MockMultipartFile jpgFile = MockMultipartFileUtils.makeMockMultipartFile("file", "/media/pic.txt");
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		params.put("type", "1");
		params.put(jpgFile.getName(), jpgFile);
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传封面文件成功() throws Exception {
		MockMultipartFile jpgFile = MockMultipartFileUtils.makeMockMultipartFile("file", "/media/1.jpg");
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		params.put("type", "1");
		params.put(jpgFile.getName(), jpgFile);
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("0", response.getCode());
	}

	//----------------------------------上传语音--------------------------------------
	@Test
	public void test上传语音文件成功() throws Exception {
		MockMultipartFile jpgFile = MockMultipartFileUtils.makeMockMultipartFile("file", "/media/1.AMR");
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		params.put("type", "0");
		params.put("duration", "10");
		params.put(jpgFile.getName(), jpgFile);
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test上传语音pctoken错误() throws Exception {
		MockMultipartFile jpgFile = MockMultipartFileUtils.makeMockMultipartFile("file", "/media/1.AMR");
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01Dn2j7i463QMWtLwp2sxyrIohd2OYjLgDXr");
		params.put("type", "0");
		params.put("duration", "10");
		params.put(jpgFile.getName(), jpgFile);
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-66", response.getCode());
	}

	@Test
	public void test上传语音session错误() throws Exception {
		MockMultipartFile jpgFile = MockMultipartFileUtils.makeMockMultipartFile("file", "/media/1.AMR");
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47f");
		params.put("pctoken", "01Dn2j7i463QMWtLwp2sxyrIohd2OYjLgDXq");
		params.put("type", "0");
		params.put("duration", "10");
		params.put(jpgFile.getName(), jpgFile);
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-76", response.getCode());
	}

	@Test
	public void test上传语音session不存在() throws Exception {
		MockMultipartFile jpgFile = MockMultipartFileUtils.makeMockMultipartFile("file", "/media/1.AMR");
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47d");
		params.put("pctoken", "01Dn2j7i463QMWtLwp2sxyrIohd2OYjLgDXq");
		params.put("type", "0");
		params.put("duration", "10");
		params.put(jpgFile.getName(), jpgFile);
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-65", response.getCode());
	}

	@Test
	public void test上传语音文件时长错误() throws Exception {
		MockMultipartFile jpgFile = MockMultipartFileUtils.makeMockMultipartFile("file", "/media/1.AMR");
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		params.put("type", "0");
		params.put(jpgFile.getName(), jpgFile);
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传语音文件错误() throws Exception {
		MockMultipartFile jpgFile = MockMultipartFileUtils.makeMockMultipartFile("file", "/media/mymp3.mp3");
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		params.put("type", "0");
		params.put(jpgFile.getName(), jpgFile);
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传语音file为空() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		params.put("type", "0");
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传语音type参数错误() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		params.put("type", "3");
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传语音type为空() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		params.put("pctoken", "01MXgmWjb4n6J8gAsf96h37TyX3qi3ETAZ8");
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传语音pctoken为空() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("session", "019973947f47c");
		String json = requestContentString("/uploadSource", params);
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test上传语音参数全为空() throws Exception {
		String json = requestContentString("/uploadSource", new HashMap<>());
		UploadSourceResponse response = JSON.parseObject(json, UploadSourceResponse.class);
		Assert.assertEquals("-3", response.getCode());
	}

}
