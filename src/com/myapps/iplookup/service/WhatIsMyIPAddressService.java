package com.myapps.iplookup.service;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.StringUtil;

import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WhatIsMyIPAddressService extends AbstractService {

    public WhatIsMyIPAddressService(DefaultHttpClient httpClient) {
        super(httpClient);
        this.baseUrl = "http://whatismyipaddress.com/ip/";
        this.priority = 1;
    }

    @Override
    public IpInfo getIpValue(String ip) {
        logger.info("Calling : " + this.toString() + ", IP: " + ip);
        IpInfo ipInfo = new IpInfo();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(getInputStream(ip)));

            String inputLine = null;
            int count = 0;
            while ((inputLine = in.readLine()) != null && count != 3) {
                if (inputLine.contains("Country")) {
                    String value = getValueFromLine(inputLine);
                    if (!StringUtil.isNullSpacesEmptyOrNA(value)) {
                        ipInfo.setCountry(value);
                        count++;
                    } else {
                        count = 0;
                        break;
                    }
                }
                if (inputLine.contains("Region")) {
                    String value = getValueFromLine(inputLine);
                    if (!StringUtil.isNullSpacesEmptyOrNA(value)) {
                        ipInfo.setRegion(value);
                        count++;
                    }
                }
                if (inputLine.contains("City")) {
                    String value = getValueFromLine(inputLine);
                    if (!StringUtil.isNullSpacesEmptyOrNA(value)) {
                        ipInfo.setCity(value);
                        count++;
                    }
                }
            }

            if (count == 0) {
                ipInfo.setErrorMsg("Cannot get country info for " + baseUrl + ip);
            }
        } catch (Exception e) {
            ipInfo.setErrorMsg(e.getMessage() + " baseUrl :" + baseUrl + ip);
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ipInfo;
    }

    private String getValueFromLine(String inputLine) throws IOException {
        inputLine = inputLine.replaceAll("\\<.*?>", "");
        String[] ret = inputLine.split(":");
        return unescapeHTML(ret[1].trim());
    }
}