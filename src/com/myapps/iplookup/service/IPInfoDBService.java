package com.myapps.iplookup.service;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

public class IPInfoDBService extends IPService {

	public IPInfoDBService() {
		this.url = "http://api.ipinfodb.com/v3/ip-country/?key=57dd7876cf42d1c9dc07574764cd29969b8ea3929f3ac38bef0f7fae330acbb9&format=json&ip=";
		setPriority(1);
		System.out.println("open connection to :" + this.url);
	}

	public IPInfoDBService(DefaultHttpClient httpclient) {
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
			int count = 0;
			entity = getContents(httpClient, new URL(finalURL));
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share
														// globally
			HashMap<String, String> jsonIPDTO = mapper.readValue(
					entity.getContent(), HashMap.class);

			for (String s : jsonIPDTO.keySet()) {
				if (s.equalsIgnoreCase("countryName")) {
					String value = jsonIPDTO.get(s);
					System.out.println("country = " + value);
					if (!StringUtil.isNullSpacesEmptyOrNA(value)
							&& !value.equals("-")) {
						ipLookup.setCountry(value);
						count++;
					} else {
						count = 0;
						break;
					}
				}
				if (s.equalsIgnoreCase("regionName")) {
					String value = jsonIPDTO.get(s);
					System.out.println(" region  =" + value);
					if (!StringUtil.isNullSpacesOrEmpty(value)
							&& !"-".equalsIgnoreCase(value)) {
						ipLookup.setRegion(value);
						count++;
					} else
						ipLookup.setRegion("N/A");
				}
				if (s.equalsIgnoreCase("cityName")) {
					String value = jsonIPDTO.get(s);
					System.out.println("city = " + value);
					if (!StringUtil.isNullSpacesOrEmpty(value)
							&& !"-".equalsIgnoreCase(value)) {
						ipLookup.setCity(value);
						count++;
					} else
						ipLookup.setCity("N/A");
				}
			}

			if (count == 0)
				ipLookup.setErrorMsg("Cannot get country info for " + finalURL);
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

}