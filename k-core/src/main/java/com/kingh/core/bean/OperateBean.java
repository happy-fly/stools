package com.kingh.core.bean;

import com.alibaba.fastjson.JSONObject;
import com.kingh.core.api.Operate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 操作
 */
public class OperateBean {

    private String id;
    private String name;
    private String description;
    private String className;
    private BundleBean bundle;
    private Class clazz;
    private String next;

    private volatile Operate operate;
    private static final Logger logger = LoggerFactory.getLogger(OperateBean.class);

    public JSONObject run(JSONObject params) {
        if (operate == null) {
            synchronized (OperateBean.class) {
                if (operate == null) {
                    try {
                        operate = (Operate) clazz.newInstance();
                    } catch (Exception e) {
                        return Operate.error(Operate.Code.operate_instance_error);
                    }
                }
            }
        }
        try {
            return operate.start(params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Operate.error(Operate.Code.server_error, e.getMessage());
        }
    }

    public void operateHelp() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public BundleBean getBundle() {
        return bundle;
    }

    public void setBundle(BundleBean bundle) {
        this.bundle = bundle;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
