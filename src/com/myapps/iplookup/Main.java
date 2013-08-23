package com.myapps.iplookup;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.IpLookupHelper;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by mohammad_abdullah on 8/1/13.
 */
public class Main {

    public static void main(String[] args) {

        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

        IpInfo ipInfo = IpLookupHelper.getIpInfo("62.135.17.146", defaultHttpClient);
        System.out.println("Country: " + ipInfo.getCountry());

        ipInfo = IpLookupHelper.getIpInfo("129.65.35.25", defaultHttpClient);
        System.out.println("Country: " + ipInfo.getCountry());

        defaultHttpClient.getConnectionManager().shutdown();
    }
}
