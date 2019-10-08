package com.kingh.core.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 组件
 */
public class BundleBean {

    private String name;
    private String fullName;
    private String version;
    private String description;
    private String author;
    private String pack = "jar";
    /**
     * 包路径
     */
    private String path;
    /**
     * 最后一次修改的时间戳，用于记录文件是否改动
     */
    private long lastUpdateTime;
    private Set<OperateBean> operates = new HashSet<>();

    public boolean start() {
        return true;
    }

    public boolean stop() {
        return true;
    }

    public boolean reload() {
        return true;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;

        if (StringUtils.isNotBlank(fullName)) {
            String[] fnames = fullName.split("\\.");
            if (fnames.length == 2) {
                if ("jar".equals(fnames[1])) {
                    String[] names = fnames[0].split("\\-");
                    int len = names.length;
                    if (len < 2) {
                        throw new RuntimeException("不合格的扩展包名称");
                    }
                    String version = names[len - 2] + "-" + names[len - 1];
                    int nameLen = len - 2;
                    String n = "";
                    for (int i = 0; i < nameLen; i++) {
                        n = n + names[i];
                    }
                    this.name = n;
                    this.version = version;
                }
            }

        }

        // ext-common-1.0-SNAPSHOT.jar

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<OperateBean> getOperates() {
        return operates;
    }

    public void setOperates(Set<OperateBean> operates) {
        this.operates = operates;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
