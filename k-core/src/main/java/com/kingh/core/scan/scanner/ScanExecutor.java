package com.kingh.core.scan.scanner;

import java.util.Set;
import java.util.function.Predicate;

public class ScanExecutor implements Scanner {
 
    private volatile static ScanExecutor instance;
 
    @Override
    public Set<Class<?>> scan(String packageName, Predicate<Class<?>> predicate) {
        Scanner fileSc = new FileScanner();
        Set<Class<?>> fileSearch = fileSc.scan(packageName, predicate);
        Scanner jarScanner = new JarScanner();
        Set<Class<?>> jarSearch = jarScanner.scan(packageName,predicate);
        fileSearch.addAll(jarSearch);
        return fileSearch;
    }
 
    private ScanExecutor(){}
 
    public static ScanExecutor getInstance(){
        if(instance == null){
            synchronized (ScanExecutor.class){
                if(instance == null){
                    instance = new ScanExecutor();
                }
            }
        }
        return instance;
    }
 
}