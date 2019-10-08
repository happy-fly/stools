package com.kingh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {


    /**
     * 获取当前时间
     *
     * @return
     */
    public static final String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }


}
