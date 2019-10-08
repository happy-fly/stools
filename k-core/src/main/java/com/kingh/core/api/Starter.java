package com.kingh.core.api;

import com.alibaba.fastjson.JSONObject;
import com.kingh.core.bean.OperateBean;
import com.kingh.core.bundle.BundleManagerFace;
import com.kingh.core.timer.SysTimerTaskExecutor;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * 调用者在启动时调用一次，用于加载资源
 */
public class Starter {

    /**
     * 扫描组件，启动定时任务
     */
    public static void start() {
        // 扫描组件
        BundleManagerFace.buildBundle();

        // 启动定时任务
        SysTimerTaskExecutor.createReloadExtLibTimerTask();
    }

    /**
     * 执行操作
     *
     * @param id
     * @param params
     * @return
     */
    public static JSONObject runOperate(String id, JSONObject params) {
        MDC.put("RID", UUID.randomUUID().toString());
        OperateBean operate = BundleManagerFace.queryOperate(id);
        if (operate != null) {
            JSONObject result = operate.run(params);
            if(result != null) {
                result.put("rid", MDC.get("RID"));
            }
            return operate.run(params);
        }
        return Operate.error(Operate.Code.no_operate);
    }


}
