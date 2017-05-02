package com.afmobi.palmcall.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
	
	public static String decode(String text) {
		Base64 objBase64 = new Base64();
		byte[] bysDecoded = objBase64.decode(text.getBytes());
		return new String(bysDecoded);
	}

	public static String encode(String text) {
		Base64 objBase64 = new Base64();
		return objBase64.encodeToString(text.getBytes());
	}
	
	public static void main(String[] args) {
		System.out.println(decode("Y2hlbmppYWxlMiB3YW50IHRvIGFkZCB5b3U="));
	}

}
