package com.myapps.iplookup.util;

import com.myapps.iplookup.service.AbstractService;
import com.myapps.iplookup.service.GeoBytesService;
import com.myapps.iplookup.service.IPInfoDBService;
import com.myapps.iplookup.service.WebyieldService;
import com.myapps.iplookup.service.WhatIsMyIPAddressService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class IpLookupHelper {

    public static IpInfo getIpInfo(final String ipAddress) {
        IpInfo ipLookup = new IpInfo();
        sortServicesByPriority();
        try {
            for (int i = 0; i < serviceList.size(); i++) {
                ipLookup = serviceList.get(i).getIpValue(ipAddress);
                if (StringUtil.isNullSpacesOrEmpty(ipLookup.getErrorMsg())) {
                    return ipLookup;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipLookup;
    }

    private static void sortServicesByPriority() {
        Collections.sort(serviceList);
    }

    private static List<AbstractService> serviceList = new LinkedList<AbstractService>();
    static {
        DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        new IPInfoDBService(httpClient, serviceList);
        new WebyieldService(httpClient, serviceList);
        new GeoBytesService(httpClient, serviceList);
        new WhatIsMyIPAddressService(httpClient, serviceList);
    }

}
