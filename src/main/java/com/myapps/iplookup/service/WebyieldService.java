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
public class WebyieldService extends AbstractService {

    public WebyieldService(DefaultHttpClient httpClient, List<AbstractService> registerList) {
        super(httpClient, registerList);
        this.baseUrl = "http://www.webyield.net/ip/index.php?ip=";
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
                ipLookup.setErrorMsg("Cannot get country info for " + baseUrl + ip);
                logger.severe(this.toString() + " " + inputLine);
                PriorityManager.getInstance().registerServiceError(this.getClass().getSimpleName());
            }

        } catch (Exception e) {
            ipLookup.setErrorMsg(e.getMessage() + " baseUrl :" + baseUrl + ip);
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
        return ipLookup;
    }

    private String getValueFromLine(BufferedReader in) throws IOException {
        String inputLine = in.readLine();
        inputLine = inputLine.replaceAll("\\<.*?>", "");
        return unescapeHTML(inputLine.trim());
    }
}