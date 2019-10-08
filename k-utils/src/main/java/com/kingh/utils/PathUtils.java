package com.kingh.utils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 用于获取当前项目的类路径
 *
 * @author <a href="https://blog.csdn.net/king_kgh>Kingh</a>
 * @version 1.0
 * @date 2019/3/19 13:20
 */
public class PathUtils {

    /**
     * 获取当前项目的类路径
     *
     * @return
     */
    public static String getClassPath() {
        try {
            String path = ClassLoader.getSystemResource("").getPath();
            if (path == null || "".equals(path)) {
                return getProjectPath();
            }
            return path;
        } catch (Exception e) {
            // 获取系统路径失败，获取项目路径
        }
        return getProjectPath();
    }

    /**
     * 获取项目所在的路径，如果运行的是jar，获取的是jar所在的路径
     *
     * @return
     */
    private static String getProjectPath() {
        URL url = PathUtils.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation();

        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), "UTF-8");
        } catch (Exception e) {
            // nothing to do
        }

        // 如果是jar，则把文件名截取掉
        if (filePath.endsWith(".jar")){
            filePath = filePath.substring(0, filePath.lastIndexOf(File.separatorChar) + 1);
        }

        return new File(filePath).getAbsolutePath();
    }
}
