package com.kingh.netty.server.netty;

import com.alibaba.fastjson.JSONObject;
import com.kingh.db.DbUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 封装http请求
 * 
 * @author lenovo
 *
 */
public class HttpRequest {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

	private FullHttpRequest request;
	private FullHttpResponse response;
	private ChannelHandlerContext ctx;

	private Map<String, String> headers;
	private JSONObject body;
	private String uri;
	private String requestId;
	private String host;

	private static QueryRunner qr;
	private static final String INSERT_HTTP_LOG = "insert into log_http_request (id, host, user_agent, content_type, content_length, path, method, content, _timestamp) values (?,?,?,?,?,?,?,?,?)";

	static {
		qr = DbUtils.getInstance("66api");
	}

	public HttpRequest(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
		this.request = request;
		this.response = response;
		this.ctx = ctx;

		this.uri = request.uri();

		this.requestId = UUID.randomUUID().toString();

		// 读取header
		HttpHeaders headers = request.headers();
		this.headers = new HashMap<>();
		for (Map.Entry<String, String> en : headers.entries()) {
			this.headers.put("header:" + en.getKey().toUpperCase(), en.getValue());
		}

		// 读取内容
		body = parseRequestBody();

		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		// 获取到客户端的IP地址
		String host = address.getHostString();
		this.host = host;

		// 保存请求流水
//		saveHttpRequest();
	}

	private void saveHttpRequest() {

		String userAgent = headers.get("header:USER-AGENT");
		String contentType = headers.get("header:CONTENT-TYPE");
		String contentLength = headers.get("header:CONTENT-LENGTH");
		String path = uri();
		String method = request.method().name();
		String content = body.toJSONString();

		try {
			qr.update(INSERT_HTTP_LOG, requestId, host, userAgent, contentType, contentLength, path, method, content,
					new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			logger.error("保存请求流水失败", e);
		}
	}

	public String uri() {
		return uri;
	}

	public String getHost() {
		return host;
	}

	public String getRequestId() {
		return requestId;
	}

	public ChannelHandlerContext getContext() {
		return ctx;
	}

	/**
	 * 根据数据方式，解析上送的数据
	 * 
	 * @return
	 */
	private JSONObject parseRequestBody() {

		// 判断头
		String contentType = headers.get("header:CONTENT-TYPE");
		if ("application/json".equals(contentType)) {
			return JSONObject.parseObject(getReqeustStr(request));
		}

		return null;
	}

	public String getParameter(String name) {
		return body.getString(name);
	}

	public JSONObject getBody() {
		return body;
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

	public FullHttpRequest getRequest() {
		return request;
	}

	public FullHttpResponse getResponse() {
		return response;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	private String getReqeustStr(FullHttpRequest request) {
		byte[] buff = new byte[request.content().capacity()];
		request.content().readBytes(buff);
		String s = new String(buff).trim();
		return s;
	}

}
