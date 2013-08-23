package com.myapps.iplookup;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.IpLookupHelper;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by mohammad_abdullah on 8/1/13.
 */
public class Test {

    public static void main(String[] args) {

        IpInfo ipInfo = IpLookupHelper.getIpInfo("123");
        System.out.println("Country: " + ipInfo.getCountry());

        ipInfo = IpLookupHelper.getIpInfo("129.65.35.25");
        System.out.println("Country: " + ipInfo.getCountry());

    }
}
