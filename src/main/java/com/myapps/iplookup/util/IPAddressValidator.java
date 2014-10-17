package com.myapps.iplookup.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mohammad_abdullah on 10/2/13.
 */
public class IPAddressValidator {

    private static IPAddressValidator INSTANCE = new IPAddressValidator();

    private static Pattern PATTERN;
    private Matcher matcher;

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static IPAddressValidator getInstance(){
        return INSTANCE;
    }

    public boolean validate(final String ip){
        matcher = PATTERN.matcher(ip);
        return matcher.matches();
    }

    static {
        PATTERN = Pattern.compile(IPADDRESS_PATTERN);
    }
    private IPAddressValidator(){}
}
