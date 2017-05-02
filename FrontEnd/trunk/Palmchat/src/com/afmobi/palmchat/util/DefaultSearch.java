package com.afmobi.palmchat.util;

import android.text.TextUtils;

public class DefaultSearch extends SearchFilter {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public char getAlpha(String str) {
		if (!TextUtils.isEmpty(str)) {
			char ch = str.charAt(0);
			if (ch >= 'A' && ch <= 'Z') {
				return ch;
			} else if (ch >= 'a' && ch <= 'z') {
				return (char)(ch - 32);
			} else {
				return '#';
			}
		}
		return '#';
	}

	@Override
	public String getFullSpell(String str) {
		if ('#' == getAlpha(str)) {
			return "|";
		}
		return str.toUpperCase();
	}

	@Override
	public String getInputString(String str) {
		return str.toUpperCase();
	}

}
