package com.kingh.config.env;

public class Env {

    /**
     * 数据库配置
     */
    private String dbConfig;

    /**
     * redis配置
     */
    private String redisConfig;

    /**
     * 服务器映射路径配置
     */
    private String serverConfig;

    /**
     * 支持的操作配置
     */
    private String bundleConfig;

    /**
     * 外部包扫描间隔时间
     */
    private Integer extLibReloadPeriod;

    public String getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(String dbConfig) {
        this.dbConfig = dbConfig;
    }

    public String getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(String redisConfig) {
        this.redisConfig = redisConfig;
    }

    public String getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(String serverConfig) {
        this.serverConfig = serverConfig;
    }

    public String getBundleConfig() {
        return bundleConfig;
    }

    public void setBundleConfig(String bundleConfig) {
        this.bundleConfig = bundleConfig;
    }

    public Integer getExtLibReloadPeriod() {
        return extLibReloadPeriod;
    }

    public void setExtLibReloadPeriod(Integer extLibReloadPeriod) {
        this.extLibReloadPeriod = extLibReloadPeriod;
    }
}
