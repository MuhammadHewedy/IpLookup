package com.myapps.iplookup;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.impl.client.DefaultHttpClient;

public class IpLookupHelper {

	private static Logger logger = Logger.getLogger(IpLookupHelper.class
			.getSimpleName());

	/**
	 * 
	 * @param ipAddress
	 * @param httpclient
	 *            reusable http client instance, usually instantiated using:
	 *            {@code new org.apache.http.impl.client.DefaultHttpClient()}
	 *            note, you should manage it on your own.
	 * 
	 * @return
	 */
	public static IpInfo getIpInfo(final String ipAddress,
			DefaultHttpClient httpclient) {
		List<SuperIPService> superIPLookUpsList = null;

		superIPLookUpsList = new LinkedList<SuperIPService>();
		superIPLookUpsList.add(new IPInfoDBService(httpclient));
		superIPLookUpsList.add(new WebyieldService(httpclient));
		superIPLookUpsList.add(new GeoBytesService(httpclient));
		superIPLookUpsList.add(new WhatIsMyIPAddressService(httpclient));

		return getIpValue(ipAddress, superIPLookUpsList);
	}

	private static IpInfo getIpValue(String ip,
			List<SuperIPService> superIPLookUps) {
		IpInfo ipLookup = new IpInfo();
		try {
			for (int i = 0; i < superIPLookUps.size(); i++) {
				logger.info("calling service: "
						+ superIPLookUps.get(i).getClass().getSimpleName());
				ipLookup = superIPLookUps.get(i).getIpValue(ip);
				if (StringUtil.isNullSpacesOrEmpty(ipLookup.getErrorMsg())) {
					return ipLookup;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipLookup;
	}

}
