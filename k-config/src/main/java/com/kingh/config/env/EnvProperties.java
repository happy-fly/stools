package com.kingh.config.env;

import java.util.Properties;

/**
 * 系统环境配置
 */
public class EnvProperties {

    private static String extClassPath;

    private static Env env = new Env();

    static {
        Properties p = new Properties();
        try {
            p.load(EnvProperties.class.getClassLoader().getResourceAsStream("config/env.properties"));
            extClassPath = p.getProperty("ext-class-path");
            env.setBundleConfig(p.getProperty("bundle-config"));
            env.setDbConfig(p.getProperty("db-config"));
            env.setRedisConfig(p.getProperty("redis-config"));
            env.setServerConfig(p.getProperty("server-config"));
            String extLibReloadTime = p.getProperty("ext-class-scan-period");
            if (extLibReloadTime == null) {
                extLibReloadTime = "60000";
            }
            env.setExtLibReloadPeriod(Integer.parseInt(extLibReloadTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Env getEnv() {
        return env;
    }

    public static String getExtClassPath() {
        return extClassPath;
    }

    public static void setExtClassPath(String extClassPath) {
        EnvProperties.extClassPath = extClassPath;
    }
}
