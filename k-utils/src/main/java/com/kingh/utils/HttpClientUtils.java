package com.kingh.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http客户端工具类
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


    public static String doPost(String url, JSONObject sendData) throws Exception {
        String result = null;
        //请求地址
        logger.info("发送请求的地址为：" + url);
        logger.info("发送请求的参数为：" + sendData);

        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
        connection.setDoOutput(true);
        connection.connect();
        OutputStream out = connection.getOutputStream();
        out.write(sendData.toJSONString().getBytes());
        out.flush();
        InputStream ins = connection.getInputStream();
        byte[] buff = new byte[ins.available()];
        ins.read(buff);
        return new String(buff);
    }

//    private static List<NameValuePair> parseJsonObject(JSONObject data) {
//        List<NameValuePair> values = new ArrayList<>();
//        values.addAll(data.entrySet().stream().map(r -> new NameValuePair() {
//            @Override
//            public String getName() {
//                return r.getKey();
//            }
//
//            @Override
//            public String getValue() {
//                Object value = r.getValue();
//                if (value == null) {
//                    return "";
//                }
//                return r.getValue().toString();
//            }
//
//            @Override
//            public String toString() {
//                return getName() + ":" + getValue();
//            }
//        }).collect(Collectors.toList()));
//        return values;
//    }

}
