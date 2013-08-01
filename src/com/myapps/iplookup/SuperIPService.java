package com.myapps.iplookup;

import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class SuperIPService {

	public SuperIPService() {
	}

	protected String url = "";
	protected DefaultHttpClient httpclient;
	protected int priority;

	public abstract IpInfo getIpValue(String ip);

	protected void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public void setHttpclient(DefaultHttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public void closeConnection() {
		httpclient.getConnectionManager().shutdown();
	}

	public static HttpEntity getContents(DefaultHttpClient httpclient, URL url)
			throws IOException {
		HttpGet httpget = new HttpGet(url.toString());
		simulateFFBrowser(httpget, url);
		HttpResponse rspns = httpclient.execute(httpget);
		HttpEntity entity = rspns.getEntity();
		return entity;
	}

	public static void simulateFFBrowser(HttpGet httpget, URL url) {
		httpget.setHeader("Host", url.getHost());
		httpget.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
		httpget.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.setHeader("Accept-Language", "en-gb,en;q=0.5");
		// actually this could return the response gziped, and the current code
		// doesn't ungzip it, see
		// this example to show how to ungzip the returned InputStream (use
		// GZipInputStream)
		// http://stackoverflow.com/questions/1573391/android-http-communication-should-use-accept-encoding-gzip#answer-1576513
		// httpget.setHeader("Accept-Encoding", "gzip, deflate");
		httpget.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		httpget.setHeader("Keep-Alive", "115");
		httpget.setHeader("Proxy-Connection", "keep-alive");
		httpget.setHeader(
				"Cookie",
				"pt=195dd2e28bf82329aa988a3311e1f61e; __gads=ID=d4fe03fdf160b2be:T=1309092327:S=ALNI_MYzgSwEq6SuIfKe00HE_2vYhSDyvQ; __utma=53830638.1203351998.1309097112.1309097112.1309100539.2; __utmc=53830638; __utmz=53830638.1309097112.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmb=53830638.1.10.1309100539");
		httpget.setHeader("Cache-Control", "max-age=0");
	}

	public static String _unescapeHTML(String in) {
		return in.replaceAll("&nbsp;", " ").replaceAll("&lt;", "<")
				.replaceAll("&gt;", ">");
	}

	@Override
	public String toString() {
		return "SuperIPService [url=" + url + "]" + ", priority= ["
				+ this.priority + "]";
	}
}