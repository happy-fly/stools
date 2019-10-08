package com.kingh.core.scan.scanner;

import java.util.Set;
import java.util.function.Predicate;

/**
 * 扫描器接口
 */
public interface Scanner {

    String CLASS_SUFFIX = ".class";

    /**
     * 扫描指定包下的符合筛选条件的类
     *
     * @param packageName 包名
     * @param predicate   筛选条件
     * @return
     */
    Set<Class<?>>scan(String packageName, Predicate<Class<?>> predicate);


    /**
     * 扫描指定包下的所有的类
     *
     * @param packageName 包名
     * @return
     */
    default Set<Class<?>> scan(String packageName) {
        return scan(packageName, null);
    }

    /**
     * 扫描
     *
     * @param basePackage
     * @param predicate
     * @return
     */
    static Set<Class<?>> scanner(String basePackage, Predicate<Class<?>> predicate) {
        return ScanExecutor.getInstance().scan(basePackage, predicate);
    }

    /**
     * 扫描
     *
     * @param basePackage
     * @return
     */
    static Set<Class<?>> scanner(String basePackage) {
        return ScanExecutor.getInstance().scan(basePackage);
    }


}