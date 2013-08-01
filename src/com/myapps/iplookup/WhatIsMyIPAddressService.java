package com.myapps.iplookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class WhatIsMyIPAddressService extends SuperIPService {
	public WhatIsMyIPAddressService() {
		this.url = "http://whatismyipaddress.com/ip/";
		setPriority(4);
		System.out.println("open connection to :" + this.url);
	}

	public WhatIsMyIPAddressService(DefaultHttpClient httpclient) {
		this();
		this.httpclient = httpclient;
	}

	@Override
	public IpInfo getIpValue(String ip) {
		String finalURL = url + ip;
		System.out.println("final url = " + finalURL);
		IpInfo ipLookup = new IpInfo();

		HttpEntity entity = null;
		BufferedReader in = null;
		try {
			entity = getContents(httpclient, new URL(finalURL));
			in = new BufferedReader(new InputStreamReader(entity.getContent()));

			String inputLine = null;
			int count = 0;
			while ((inputLine = in.readLine()) != null && count != 3) {
				if (inputLine.contains("Country")) {
					String value = getValueFromLine(inputLine);
					if (!StringUtil.isNullSpacesEmptyOrNA(value)) {
						ipLookup.setCountry(value);
						count++;
					} else {
						count = 0;
						break;
					}

				}
				if (inputLine.contains("Region")) {
					String value = getValueFromLine(inputLine);
					if (!StringUtil.isNullSpacesEmptyOrNA(value)) {
						ipLookup.setRegion(value);
						count++;
					}
				}
				if (inputLine.contains("City")) {
					String value = getValueFromLine(inputLine);
					if (!StringUtil.isNullSpacesEmptyOrNA(value)) {
						ipLookup.setCity(value);
						count++;
					}

				}
			}

			if (count == 0) {
				ipLookup.setErrorMsg("Cannot get country info for " + finalURL);
				// return new ArrayList<String>();
			}
			System.out.println("url 2 " + ipLookup.toString());
		} catch (Exception e) {
			ipLookup.setErrorMsg(e.getMessage() + " url :" + finalURL);
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (entity != null)
					EntityUtils.consume(entity);
			} catch (IOException e) {

			}
		}
		return ipLookup;
	}

	private String getValueFromLine(String inputLine) throws IOException {
		inputLine = inputLine.replaceAll("\\<.*?>", "");
		String[] ret = inputLine.split(":");
		return _unescapeHTML(ret[1].trim());
	}

}