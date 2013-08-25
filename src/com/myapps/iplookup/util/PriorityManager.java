package com.myapps.iplookup.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by muhammad on 8/25/13.
 */
public class PriorityManager {

    private static PriorityManager INSTANCE = new PriorityManager();
    private static String FILE_PATH = System.getProperty("user.home") + "/iplookup.dat";

    private static Properties properties = new Properties();

    private PriorityManager() {
    }

    public static PriorityManager getInstance(){
        return INSTANCE;
    }

    public int getPriority(String serviceName){
        String p = properties.getProperty(serviceName, "0");
        System.out.println("s : " + serviceName + ", p: " + p);
        return Integer.parseInt(p);
    }

    public void registerServiceError(String serviceName){
        String p = properties.getProperty(serviceName, "0");
        String v = String.valueOf((Integer.parseInt(p) + 1));
        properties.setProperty(serviceName, v);

        try {
            properties.store(new FileOutputStream(FILE_PATH), serviceName + " : " + v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            File f = new File(FILE_PATH);
            if (!f.exists()){
                f.createNewFile();
            }
            properties.load(new FileInputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
