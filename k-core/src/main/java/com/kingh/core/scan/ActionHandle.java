package com.kingh.core.scan;


import com.kingh.core.anno.Action;
import com.kingh.core.scan.scanner.ScanExecutor;
import com.kingh.core.scan.scanner.Scanner;

import java.util.Set;

public class ActionHandle {

    public static void main(String[] args) throws Exception {
        Scanner s = ScanExecutor.getInstance();
        Set<Class<?>> classList = s.scan("");

        for (Class clazz : classList) {
            boolean flag = clazz.isAnnotationPresent(Action.class);
            if (flag) {
                Action action = (Action) clazz.getAnnotation(Action.class);
                System.out.println(action.name());
            }
        }
        System.out.println(classList);
    }

}
