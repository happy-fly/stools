package com.kingh.core.scan;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 扫描门面，所有扫描，类加载相关操作均通过此类调用
 */
public class ScanFace {

    /**
     * 列出目录下的所有文件
     *
     * @param path
     * @param c
     * @return
     */
    public static Set<File> listDirFiles(String path, Predicate<String> c) {
        if (StringUtils.isBlank(path)) {
            throw new NullPointerException("path is null");
        }
        Predicate<String> t = c == null ? x -> true : c;
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            return Arrays
                    .stream(files)
                    .filter(f -> t.test(f.getName()))
                    .collect(Collectors.toSet());
        } else {
            throw new RuntimeException("指定的路径不存在，或者不是一个有效的目录 " + path);
        }
    }

    /**
     * 列出jar包中的所有文件（包括目录）
     *
     * @param jarFilePath
     * @param t
     * @return
     */
    public static Set<String> listJarFile(String jarFilePath, Predicate<String> t) {
        if (jarFilePath == null) {
            throw new NullPointerException("jar file path is null");
        }
        Predicate<String> c = t == null ? x -> true : t;

        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entries = jarFile.entries();
            JarEntry entry;
            Set<String> files = new HashSet<>();
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                String fname = entry.getName();
                if (c.test(fname)) {
                    files.add(entry.getName());
                }
            }
            return files;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载外部jar中的class
     *
     * @param jarFilePath
     * @param className
     * @return
     */
    public static Class<?> loadExtJarClass(String jarFilePath, String className) {
        File jarFile = new File(jarFilePath);
        try {
            URL url = jarFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
            return classLoader.loadClass(className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
