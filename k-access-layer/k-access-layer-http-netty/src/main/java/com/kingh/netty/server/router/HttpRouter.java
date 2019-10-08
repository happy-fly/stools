package com.kingh.netty.server.router;

import com.alibaba.fastjson.JSONObject;
import com.kingh.netty.server.config.ActionBean;
import com.kingh.netty.server.netty.HttpRequest;
import com.kingh.netty.server.config.ServerConfig;
import com.kingh.netty.server.handler.Handler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由
 *
 * @author 孔冠华
 */
public class HttpRouter {

    private static Logger logger = LoggerFactory.getLogger(HttpRouter.class);

    /**
     * 所有的实例化后的处理器
     */
    private static final ConcurrentHashMap<String, Handler> handlers = new ConcurrentHashMap<>();

    /**
     * 所有系统配置的处理器
     */
    private static final Map<String, ActionBean> ACTIONS = ServerConfig.getActions();

    public static void route(HttpRequest request) {

        // 获取请求的地址
        String url = request.uri();
        ActionBean ab = ACTIONS.get(url);

        logger.info("本次请求的处理器为：" + JSONObject.toJSONString(ab));

        // 获取到Response
        FullHttpResponse response = request.getResponse();

        // 404
        if (ab == null) {
            response.content().writeBytes("<h1>404 Page Not Found !</h1>".getBytes());
            response.setStatus(HttpResponseStatus.NOT_FOUND);
            return;
        }

        Handler handler = null;
        try {
            handler = handlers.get(ab.getClassName());
            if (handler == null) {
                synchronized (HttpRouter.class) {
                    if (handler == null) {
                        handler = (Handler) Class.forName(ab.getClassName()).newInstance();
                        handlers.put(ab.getClassName(), handler);
                    }
                }
            }
            handler.handle(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
