package com.myapps.iplookup;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.IpLookupHelper;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by mohammad_abdullah on 8/1/13.
 */
public class Test {

    public static void main(String[] args) {

        for (int i = 100; i <= 150; i++) {
            printInfo(i);
        }
    }

    public static void printInfo(int i){
        IpInfo ipInfo = IpLookupHelper.getIpInfo("80." + i + ".35." + i);

        String errorMsg = ipInfo.getErrorMsg();

        if (errorMsg != null) {
            System.out.println("Error: " + errorMsg);
        } else {
            System.out.println("Country: " + ipInfo.getCountry() + ", " + ipInfo.getCity() +
                    ", " + ipInfo.getRegion());
        }
    }
}
