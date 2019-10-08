package com.kingh.netty.server.netty;

import com.kingh.netty.server.config.ServerConfig;
import com.kingh.netty.server.router.HttpRouter;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 核心服务处理器
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

	static {
		// 加载路由配置
		ServerConfig.init();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

		if (!request.decoderResult().isSuccess()) {
			Throwable t = request.decoderResult().cause();
			if (t != null) {
				logger.error("解析请求报文出错！", t);
			}
			response.setStatus(HttpResponseStatus.BAD_REQUEST);
		}

		// 限制请求方法
		HttpMethod httpMethod = request.method();
		if (httpMethod.compareTo(HttpMethod.POST) != 0) {
			response.content().writeBytes("METHOD NOT ALLOWED".getBytes());
			response.setStatus(HttpResponseStatus.METHOD_NOT_ALLOWED);
		}
		
		// TODO 白名单

		HttpRequest reqWrapper = new HttpRequest(ctx, request, response);
		if (response.status() == HttpResponseStatus.OK) {
			HttpRouter.route(reqWrapper);
		}

		ctx.writeAndFlush(reqWrapper.getResponse()).addListener(ChannelFutureListener.CLOSE);
	}

}
