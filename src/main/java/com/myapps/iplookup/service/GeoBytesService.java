package com.myapps.iplookup.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.PriorityManager;

@SuppressWarnings("deprecation")
public class GeoBytesService extends AbstractService {

	public GeoBytesService(DefaultHttpClient httpClient,
			List<AbstractService> registerList) {
        super(httpClient, registerList);
        this.baseUrl = "http://www.geobytes.com/IpLocator.htm?GetLocation&IpAddress=";
    }

    @Override
    public IpInfo getIpValue(String ip) {
        logger.info("Calling : " + this.toString() + ", IP: " + ip);
        IpInfo ipLookup = new IpInfo();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(getInputStream(ip)));

            String inputLine = null;
            int count = 0;
            String finalInputLine = " ";
            while ((inputLine = in.readLine()) != null && count != 3) {
                if (inputLine.contains("ro-no_bots_pls")) {
                    finalInputLine += inputLine;
                }
            }
            ipLookup = jsoupParser(finalInputLine);
        } catch (Exception e) {
            ipLookup.setErrorMsg(e.getMessage() + " baseUrl :" + baseUrl + ip);
            logger.severe(e.getMessage());
            PriorityManager.getInstance().registerServiceError(this.getClass().getSimpleName());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }

        updatePriority();
        return ipLookup;
    }

    private IpInfo jsoupParser(String Html) {
        IpInfo ipLookup = new IpInfo();
        int count = 0;
        Document doc = Jsoup.parse(Html);
        Elements e = doc.getElementsByTag("input");
        for (int i = 0; i < e.size(); i++) {
            if (i == 1) {
                Element element = e.get(i);
                String nodeValue = element.attr("value");
                if (!nodeValue.equalsIgnoreCase("Limit Exceeded")) {
                    ipLookup.setCountry(nodeValue);
                    count++;
                } else {
                    count = 0;
                    break;
                }
            }
            if (i == 3) {
                Element element = e.get(i);
                String nodeValue = element.attr("value");
                ipLookup.setRegion(nodeValue);
                count++;
            }
            if (i == 4) {
                Element element = e.get(i);
                String nodeValue = element.attr("value");
                ipLookup.setCity(nodeValue);
                count++;
            }
        }
        if (count == 0) {
            ipLookup.setErrorMsg("Cannot get country info for " + baseUrl);
            logger.severe(this.toString() + " " + Html);
            PriorityManager.getInstance().registerServiceError(this.getClass().getSimpleName());
        }
        return ipLookup;
    }

}