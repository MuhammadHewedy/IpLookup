package com.myapps.iplookup.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import sun.misc.IOUtils;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.PriorityManager;
import com.myapps.iplookup.util.StringUtil;

@SuppressWarnings({ "deprecation", "restriction" })
public class IPInfoDBService extends AbstractService {

    public IPInfoDBService(DefaultHttpClient httpClient, List<AbstractService> registerList) {
        super(httpClient, registerList);
        this.baseUrl = "http://api.ipinfodb.com/v3/ip-city/?key=57dd7876cf42d1c9dc07574764cd29969b8ea3929f3ac38bef0f7fae330acbb9&format=json&ip=";
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
			@SuppressWarnings("unchecked")
			HashMap<String, String> jsonIPDTO = mapper.readValue(in,
					HashMap.class);

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
                PriorityManager.getInstance().registerServiceError(this.getClass().getSimpleName());
                ipLookup.setErrorMsg("Cannot get country info for " + baseUrl + ip);
                logger.severe(this.toString() + " " +
                        new String(IOUtils.readFully(in, 1000, true), "UTF-8"));

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
}