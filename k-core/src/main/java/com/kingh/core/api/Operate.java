package com.kingh.core.api;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.slf4j.MDC;

/**
 * 核心接口，所有操作均需要实现此接口
 */
public interface Operate {

    /**
     * 实现操作的方法
     *
     * @param params
     * @return
     * @throws Exception
     */
    JSONObject start(JSONObject params) throws Exception;

    /**
     * 帮助文档
     *
     * @return
     * @throws Exception
     */
    default String help() throws Exception {
        return "作者比较懒，没有任何帮助信息提供！";
    }

    /**
     * 错误消息
     */
    enum Code {
        /**
         * 服务器错误
         */
        server_error("500", "服务器错误"),

        /**
         * 找不到操作组件
         */
        no_operate("404", "指定操作的ID不存在"),

        /**
         * 参数校验不通过
         */
        check_error("505", "参数校验不通过"),

        /**
         * 操作实现类实例化失败
         */
        operate_instance_error("501", "操作实例化失败");


        private String code;
        private String msg;

        Code(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return this.code;
        }

        public String getMsg() {
            return this.msg;
        }
    }

    /**
     * 快速错误响应
     *
     * @param code
     * @return
     */
    static JSONObject error(Code code) {
        return result(code.getCode(), code.getMsg(), null, null);
    }

    /**
     * 详细错误响应
     *
     * @param code
     * @return
     */
    static JSONObject error(Code code, String description) {
        return result(code.getCode(), code.getMsg(), description, null);
    }

    /**
     * 标准响应
     *
     * @param code
     * @param msg
     * @param description
     * @param data
     * @return
     */
    static JSONObject result(String code, String msg, String description, JSONObject data) {
        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("msg", msg);
        result.put("description", description);
        result.put("mills", System.currentTimeMillis());
        result.put("data", data);
        result.put("rid", MDC.get("RID"));
        return result;
    }

}
