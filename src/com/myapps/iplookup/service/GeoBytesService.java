package com.myapps.iplookup.service;

import com.myapps.iplookup.util.IpInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GeoBytesService extends IPService {

	public GeoBytesService() {
		this.url = "http://www.geobytes.com/IpLocator.htm?GetLocation&IpAddress=";
		setPriority(3);
		System.out.println("open connection to :" + this.url);
	}

	public GeoBytesService(DefaultHttpClient httpclient) {
		this();
		this.httpClient = httpclient;
	}

	private String _finalURL = "";

	@Override
	public IpInfo getIpValue(String ip) {

		String finalURL = this.url + ip;
		_finalURL = finalURL;

		System.out.println("final url = " + finalURL);
		IpInfo ipLookup = new IpInfo();

		HttpEntity entity = null;
		BufferedReader in = null;

		try {
			entity = getContents(httpClient, new URL(finalURL));
			in = new BufferedReader(new InputStreamReader(entity.getContent()));

			String inputLine = null;
			int count = 0;
			String finalInputLine = " ";
			while ((inputLine = in.readLine()) != null && count != 3) {
				if (inputLine.contains("ro-no_bots_pls")) {
					finalInputLine += inputLine;
				}
			}
			// System.out.println("finalInputLine= "+finalInputLine);
			// System.out.println(jsoupParser(finalInputLine));
			ipLookup = jsoupParser(finalInputLine);
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

	// private String getValueFromLine(String in) throws IOException {
	// String inputLine = in;
	// jsoupParser(inputLine);
	// return _unescapeHTML(inputLine.trim());
	// }
	private IpInfo jsoupParser(String Html) {
		IpInfo ipLookup = new IpInfo();
		int count = 0;
		Document doc = Jsoup.parse(Html);
		Elements e = doc.getElementsByTag("input");
		for (int i = 0; i < e.size(); i++) {
			if (i == 1) {
				Element elemen = e.get(i);
				String nodeValue = elemen.attr("value");
				if (!nodeValue.equalsIgnoreCase("Limit Exceeded")) {
					ipLookup.setCountry(nodeValue);
					count++;
				} else {
					count = 0;
					break;
				}
			}
			if (i == 3) {
				Element elemen = e.get(i);
				String nodeValue = elemen.attr("value");
				ipLookup.setRegion(nodeValue);
				count++;
				// System.out.println(nodeValue);
			}
			if (i == 4) {
				Element elemen = e.get(i);
				String nodeValue = elemen.attr("value");
				ipLookup.setCity(nodeValue);
				count++;
				// System.out.println(nodeValue);
			}
		}
		if (count == 0) {
			ipLookup.setErrorMsg("Cannot get country info for " + _finalURL);
		}
		return ipLookup;
	}

}