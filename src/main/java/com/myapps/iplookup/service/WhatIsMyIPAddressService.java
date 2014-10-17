package com.myapps.iplookup.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.PriorityManager;
import com.myapps.iplookup.util.StringUtil;

@SuppressWarnings("deprecation")
public class WhatIsMyIPAddressService extends AbstractService {

    public WhatIsMyIPAddressService(DefaultHttpClient httpClient, List<AbstractService> registerList) {
        super(httpClient, registerList);
        this.baseUrl = "http://whatismyipaddress.com/ip/";
    }

    @Override
    public IpInfo getIpValue(String ip) {
        logger.info("Calling : " + this.toString() + ", IP: " + ip);
        IpInfo ipInfo = new IpInfo();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(getInputStream(ip)));
            String inputLine, msg = "";
            int count = 0;
            while ((inputLine = in.readLine()) != null && count != 3) {
                msg += inputLine;
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
                logger.severe(this + " " + msg);
                PriorityManager.getInstance().registerServiceError(this.getClass().getSimpleName());
            }
        } catch (Exception e) {
            ipInfo.setErrorMsg(e.getMessage() + " baseUrl :" + baseUrl + ip);
            logger.severe(e.getMessage());
            PriorityManager.getInstance().registerServiceError(this.getClass().getSimpleName());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updatePriority();
        return ipInfo;
    }

    private String getValueFromLine(String inputLine) throws IOException {
        inputLine = inputLine.replaceAll("\\<.*?>", "");
        String[] ret = inputLine.split(":");
        return unescapeHTML(ret[1].trim());
    }
}