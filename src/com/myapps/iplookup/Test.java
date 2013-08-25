package com.myapps.iplookup;

import com.myapps.iplookup.util.IpInfo;
import com.myapps.iplookup.util.IpLookupHelper;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by mohammad_abdullah on 8/1/13.
 */
public class Test {

    public static void main(String[] args) {

        for (int i = 0; i <= 255; i++) {
            final int aii = i;
            Thread thd = new Thread(){
                @Override
                public void run() {
                    printInfo(aii);
                }
            };
            thd.start();

        }
    }

    public static void printInfo(int i){
        IpInfo ipInfo = IpLookupHelper.getIpInfo("129.65.35." + i);

        String errorMsg = ipInfo.getErrorMsg();

        if (errorMsg != null) {
            System.out.println("Error: " + errorMsg);
        } else {
            System.out.println("Country: " + ipInfo.getCountry() + ", " + ipInfo.getCity() +
                    ", " + ipInfo.getRegion());
        }
    }
}
