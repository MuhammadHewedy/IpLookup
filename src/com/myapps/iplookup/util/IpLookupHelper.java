package com.myapps.iplookup.util;

import com.myapps.iplookup.service.GeoBytesService;
import com.myapps.iplookup.service.IPService;
import com.myapps.iplookup.service.IPInfoDBService;
import com.myapps.iplookup.service.WebyieldService;
import com.myapps.iplookup.service.WhatIsMyIPAddressService;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.impl.client.DefaultHttpClient;

public class IpLookupHelper {

    private static Logger logger = Logger.getLogger(IpLookupHelper.class
            .getSimpleName());

    /**
     * @param ipAddress
     * @param httpClient reusable http client instance, usually instantiated using:
     *                   {@code new org.apache.http.impl.client.DefaultHttpClient()}
     *                   note, you should manage it on your own.
     * @return
     */
    public static IpInfo getIpInfo(final String ipAddress,
                                   DefaultHttpClient httpClient) {
        List<IPService> superIPLookUpsList = new LinkedList<IPService>();
        superIPLookUpsList.add(new IPInfoDBService(httpClient));
        superIPLookUpsList.add(new WebyieldService(httpClient));
        superIPLookUpsList.add(new GeoBytesService(httpClient));
        superIPLookUpsList.add(new WhatIsMyIPAddressService(httpClient));

        return getIpValue(ipAddress, superIPLookUpsList);
    }

    private static IpInfo getIpValue(String ip,
                                     List<IPService> superIPLookUps) {
        IpInfo ipLookup = new IpInfo();
        try {
            for (int i = 0; i < superIPLookUps.size(); i++) {
                logger.info("calling service: "
                        + superIPLookUps.get(i).getClass().getSimpleName());
                ipLookup = superIPLookUps.get(i).getIpValue(ip);
                if (StringUtil.isNullSpacesOrEmpty(ipLookup.getErrorMsg())) {
                    return ipLookup;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipLookup;
    }

}
