package com.myapps.iplookup;

public class ArraysUtil {
	public static boolean in(String[] list, String in) {
		if (list == null || list.length == 0
				|| StringUtil.isNullSpacesOrEmpty(in))
			return false;

		for (String s : list)
			if (s.equalsIgnoreCase(in))
				return true;
		return false;
	}
}