package com.myapps.iplookup.service;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.StringUtil;

import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import sun.misc.IOUtils;

public class IPInfoDBService extends AbstractService {

    public IPInfoDBService(DefaultHttpClient httpClient, List<AbstractService> registerList) {
        super(httpClient, registerList);
        this.baseUrl = "http://api.ipinfodb.com/v3/ip-country/?key=57dd7876cf42d1c9dc07574764cd29969b8ea3929f3ac38bef0f7fae330acbb9&format=json&ip=";
        this.priority = 4;
    }

    @Override
    public IpInfo getIpValue(String ip) {
        logger.info("Calling : " + this.toString() + ", IP: " + ip);
        IpInfo ipLookup = new IpInfo();
        InputStream in = null;
        try {
            int count = 0;
            ObjectMapper mapper = new ObjectMapper();
            in = getInputStream(ip);
            HashMap<String, String> jsonIPDTO = mapper.readValue(in, HashMap.class);

            for (String s : jsonIPDTO.keySet()) {
                if (s.equalsIgnoreCase("countryName")) {
                    String value = jsonIPDTO.get(s);
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
                    if (!StringUtil.isNullSpacesOrEmpty(value)
                            && !"-".equalsIgnoreCase(value)) {
                        ipLookup.setRegion(value);
                        count++;
                    } else
                        ipLookup.setRegion("N/A");
                }
                if (s.equalsIgnoreCase("cityName")) {
                    String value = jsonIPDTO.get(s);
                    if (!StringUtil.isNullSpacesOrEmpty(value)
                            && !"-".equalsIgnoreCase(value)) {
                        ipLookup.setCity(value);
                        count++;
                    } else
                        ipLookup.setCity("N/A");
                }
            }

            if (count == 0){
                ipLookup.setErrorMsg("Cannot get country info for " + baseUrl + ip);
                logger.severe(this.toString() + " " +
                        new String(IOUtils.readFully(in, in.available(), true), "UTF-8"));
            }
        } catch (Exception e) {
            ipLookup.setErrorMsg(e.getMessage() + " baseUrl :" + baseUrl + ip);
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ipLookup;
    }
}