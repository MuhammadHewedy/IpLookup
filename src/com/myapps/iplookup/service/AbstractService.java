package com.myapps.iplookup.service;

import com.myapps.iplookup.util.IpInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractService implements Comparable<AbstractService> {

    protected static final Logger logger = Logger.getLogger(AbstractService.class.getSimpleName());
    protected static int LOWEST_PRIORITY = Integer.MAX_VALUE;

    protected String baseUrl;
    protected DefaultHttpClient httpClient;
    protected int priority = LOWEST_PRIORITY;

    public AbstractService(DefaultHttpClient httpClient, List<AbstractService> registerList) {
        this.httpClient = httpClient;
        registerList.add(this);
    }

    public abstract IpInfo getIpValue(String ip);

    protected InputStream getInputStream(String ipAddress)
            throws IOException {
        URL url = new URL(baseUrl + ipAddress);
        HttpGet httpget = new HttpGet(url.toString());
        appendHeaders(httpget, url);
        HttpResponse response = httpClient.execute(httpget);
        HttpEntity entity = response.getEntity();
        return entity.getContent();
    }

    private void appendHeaders(HttpGet httpget, URL url) {
        httpget.setHeader("Host", url.getHost());
        httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpget.setHeader("Accept-Language", "en-gb,en;q=0.5");
        httpget.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        httpget.setHeader("Keep-Alive", "115");
        httpget.setHeader("Proxy-Connection", "keep-alive");
        httpget.setHeader("Cache-Control", "max-age=0");
    }

    public static String unescapeHTML(String in) {
        return in.replaceAll("&nbsp;", " ").replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">");
    }

    @Override
    public int compareTo(AbstractService ipService) {
        return new Integer(this.priority).compareTo(ipService.priority);
    }

    @Override
    public String toString() {
        return "AbstractService [baseUrl=" + baseUrl + "]" + ", priority= ["
                + this.priority + "]";
    }
}