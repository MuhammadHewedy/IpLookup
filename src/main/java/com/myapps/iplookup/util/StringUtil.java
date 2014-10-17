package com.myapps.iplookup.util;

public class StringUtil {
	private static String SINGLE_QUOTE = "'";

	public static boolean isNullSpacesOrEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static boolean isNullSpacesOrEmptyOrMinusOne(String s) {
		boolean b = (s == null || s.trim().length() == 0);
		try {
			return b || (-1 == Integer.parseInt(s));
		} catch (Exception ex) {
		}
		return false;
	}

	public static boolean isNullSpacesEmptyOrNA(String s) {
		return isNullSpacesOrEmpty(s) || "n/a".equalsIgnoreCase(s);
	}

	public static String safeToString(Object o) {
		if (o != null)
			return o.toString();
		return "";
	}

	public static String singleQuote(String s) {
		return SINGLE_QUOTE + s + SINGLE_QUOTE;
	}

	public static String removeSpecialCharacters(String str, char[] execluded) {

		char[] chars = str.toCharArray();
		char[] tmp = new char[chars.length];

		int j = 0;
		for (int i = 0; i < chars.length; i++) {
			if (isDigit(chars[i]) || isLower(chars[i]) || isUpper(chars[i])
					|| in(chars[i], execluded))
				tmp[j++] = chars[i];
		}

		int size = 0;
		for (int i = 0; i < tmp.length; i++)
			if ((int) tmp[i] != 0)
				size++;

		return new String(tmp, 0, size);
	}

	public static String removeSpecialCharacters(String str) {
		return removeSpecialCharacters(str, null);
	}

	public static boolean isDigit(char ch) {
		return ch >= 48 && ch <= 57;
	}

	public static boolean isLower(char ch) {
		return ch >= 97 && ch <= 122;
	}

	public static boolean isUpper(char ch) {
		return ch >= 65 && ch <= 90;
	}

	public static boolean in(char ch, char[] ins) {
		for (int i = 0; i < ins.length; i++)
			if (ch == ins[i])
				return true;
		return false;
	}

	// XXX use this to replace removeSpecialCharacters(str, new char[] {'#',
	// '.', '+'}
	public static String getNanPartnum(String str) {
		return removeSpecialCharacters(str, new char[] { '#', '.', '+' });
	}
}