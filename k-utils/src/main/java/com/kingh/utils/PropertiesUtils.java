package com.kingh.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    public static Properties getProperties(InputStream ins) {
        Properties p = new Properties();
        try {
            p.load(ins);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

}
