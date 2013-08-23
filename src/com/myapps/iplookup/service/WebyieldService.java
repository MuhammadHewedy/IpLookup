package com.myapps.iplookup.service;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class WebyieldService extends IPService {

	public WebyieldService() {
		this.url = "http://www.webyield.net/ip/index.php?ip=";
		setPriority(2);
		System.out.println("open connection to :" + this.url);
	}

	public WebyieldService(DefaultHttpClient httpclient) {
		this();
		this.httpClient = httpclient;
	}

	@Override
	public IpInfo getIpValue(String ip) {

		String finalURL = url + ip;
		System.out.println("final url = " + finalURL);
		IpInfo ipLookup = new IpInfo();

		HttpEntity entity = null;
		BufferedReader in = null;

		try {
			entity = getContents(httpClient, new URL(finalURL));
			in = new BufferedReader(new InputStreamReader(entity.getContent()));

			String inputLine = null;
			int count = 0;
			while ((inputLine = in.readLine()) != null && count != 3) {
				if (inputLine.contains("Country Name")) {
					String value = getValueFromLine(in);
					if (!StringUtil.isNullSpacesEmptyOrNA(value)) {
						ipLookup.setCountry(value);
						count++;
					} else {
						count = 0;
						break;
					}
				}
				if (inputLine.contains("Region")) {
					String value = getValueFromLine(in);
					if (!StringUtil.isNullSpacesOrEmpty(value)) {
						ipLookup.setRegion(value);
						count++;
					}
				}
				if (inputLine.contains("City")) {
					String value = getValueFromLine(in);
					if (!StringUtil.isNullSpacesOrEmpty(value)) {
						ipLookup.setCity(value);
						count++;
					}
				}
			}

			if (count == 0) {
				ipLookup.setErrorMsg("Cannot get country info for " + finalURL);
			}

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

	private String getValueFromLine(BufferedReader in) throws IOException {
		String inputLine = in.readLine();
		inputLine = inputLine.replaceAll("\\<.*?>", "");
		return _unescapeHTML(inputLine.trim());
	}
}