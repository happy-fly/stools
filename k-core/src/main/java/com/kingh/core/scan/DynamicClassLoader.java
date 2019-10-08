package com.kingh.core.scan;

import com.kingh.config.env.EnvProperties;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;

public class DynamicClassLoader extends ClassLoader {


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        try {
            // 双亲委派
            Class<?> clazz = super.loadClass(name);
            return clazz;
        } catch (ClassNotFoundException e) {

            // 到扩展路径下去进行加载
            String extClassPath = EnvProperties.getExtClassPath();
            File fdir = new File(extClassPath);
            if (fdir.exists() && fdir.isDirectory()) {
                // 开始加载类
                return load(name, extClassPath);
            } else {
                throw new RuntimeException("指定的扩展路径不存在或者不是一个文件");
            }
        }
    }

    private Class<T> load(String name, String fdir) {

        File dir = new File(fdir);

        // 查找文件夹

        // 查找jar



        return null;
    }
}
