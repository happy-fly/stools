package com.kingh.core.bundles;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class ResultHandle {

	public static Bean handle(JSONObject result) {
		Integer code = result.getInteger("code");

		// 文件下载
		if (1 == code) {
			byte[] data = result.getString("").getBytes();

			Map<String, String> header = Maps.newHashMap(Bean.DOWNLOAD);
			header.put("Accept-Length", data.length+"");
			header.put("Content-Disposition", "attachment;filename=" + result.getString("fname"));
			return new Bean(data, header);
		} else {
			return new Bean(result.toJSONString().getBytes(), Bean.JSON);
		}

	}

	public static class Bean {
		
		static final Map<String, String> JSON = new HashMap<>();
		static final Map<String, String> DOWNLOAD = new HashMap<>();
		static {
			JSON.put("Content-Type", "application/json");
			DOWNLOAD.put("Content-Type", "application/octet-stream");
		}
		
		private byte[] buff;
		private Map<String, String> header;

		public Bean(byte[] buff, Map<String, String> header) {
			this.buff = buff;
			this.header = header;
		}

		public byte[] getBuff() {
			return buff;
		}

		public void setBuff(byte[] buff) {
			this.buff = buff;
		}

		public Map<String, String> getHeader() {
			return header;
		}

		public void setHeader(Map<String, String> header) {
			this.header = header;
		}

	}

}
