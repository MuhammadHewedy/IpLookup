package com.myapps.iplookup.service;

import com.myapps.iplookup.util.IpInfo;

import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class IPService implements Comparable<IPService>{

	public IPService() {
	}

	protected String url = "";
	protected DefaultHttpClient httpClient;
	protected int priority;

	public abstract IpInfo getIpValue(String ip);

	protected void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public static HttpEntity getContents(DefaultHttpClient httpClient, URL url)
			throws IOException {
		HttpGet httpget = new HttpGet(url.toString());
		simulateFFBrowser(httpget, url);
		HttpResponse response = httpClient.execute(httpget);
		HttpEntity entity = response.getEntity();
		return entity;
	}

	public static void simulateFFBrowser(HttpGet httpget, URL url) {
		httpget.setHeader("Host", url.getHost());
		httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.setHeader("Accept-Language", "en-gb,en;q=0.5");
		httpget.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		httpget.setHeader("Keep-Alive", "115");
		httpget.setHeader("Proxy-Connection", "keep-alive");
		httpget.setHeader("Cache-Control", "max-age=0");
	}

	public static String _unescapeHTML(String in) {
		return in.replaceAll("&nbsp;", " ").replaceAll("&lt;", "<")
				.replaceAll("&gt;", ">");
	}

    @Override
    public int compareTo(IPService ipService) {
        return new Integer(this.getPriority()).compareTo(new Integer(ipService.getPriority()));
    }

    @Override
	public String toString() {
		return "IPService [url=" + url + "]" + ", priority= ["
				+ this.priority + "]";
	}
}