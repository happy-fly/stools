package com.kingh.netty.server.handler.system;

import com.alibaba.fastjson.JSONObject;
import com.kingh.core.api.Starter;
import com.kingh.core.bundles.ResultHandle;
import com.kingh.netty.server.netty.HttpRequest;
import com.kingh.netty.server.handler.Handler;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class OperateHandle implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(OperateHandle.class);

    static {
        Starter.start();
    }

    @Override
    public void handle(HttpRequest request) {
        logger.info("Operate 接收到请求，请求的数据为：" + request.getBody().toJSONString());

        JSONObject config = request.getBody();
        FullHttpResponse response = request.getResponse();
        try {
            // json数据格式，正常处理
            String operateId = config.getString("operate");

            config.putAll(request.getHeaders());
            config.put("clientIp", request.getHost());
            JSONObject result = Starter.runOperate(operateId, config);

            // 处理result
            ResultHandle.Bean bean = ResultHandle.handle(result);

            // 响应
            Map<String, String> header = bean.getHeader();
            for (Map.Entry<String, String> en : header.entrySet()) {
                response.headers().add(en.getKey(), en.getValue());
            }
            response.content().writeBytes(bean.getBuff());
        } catch (Exception e) {
            // logger.info("执行文件上传操作！");
            // // 非格式化数据，直接上传到服务器，并生成文件唯一标识，通过文件唯一标识，可以定位文件
            // String fid = UUID.randomUUID().toString();
            // String path = "/data/";
            //
            // PrintWriter pw = null;
            // try {
            // pw = new PrintWriter(new File(path + fid));
            // pw.println(config.getString("data"));
            // pw.flush();
            // logger.info("保存文件成功，文件标识为：" + fid);
            //
            // JSONObject result = new JSONObject();
            // result.put("fid", fid);
            // result.put("ts", System.currentTimeMillis());
            // response.headers().add("Content-Type", "application/json");
            // response.content().writeBytes(result.toString().getBytes());
            //
            // } catch (FileNotFoundException e1) {
            // e1.printStackTrace();
            // } finally {
            // if (pw != null) {
            // pw.close();
            // }
            // }
        }

    }
}
