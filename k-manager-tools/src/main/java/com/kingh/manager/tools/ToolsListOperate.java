package com.kingh.manager.tools;

import com.alibaba.fastjson.JSONObject;
import com.kingh.core.anno.Action;
import com.kingh.core.api.Operate;
import com.kingh.core.bean.OperateBean;
import com.kingh.core.bundle.BundleManagerFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

@Action(id = "ol", name = "工具列表")
public class ToolsListOperate implements Operate {

    private static final Logger logger = LoggerFactory.getLogger(ToolsListOperate.class);

    @Override
    public JSONObject start(JSONObject params) throws Exception {
        String bundleName = params.getString("bundle");
        String version = params.getString("version");

        String fullName = bundleName + "-" + version + ".jar";
        logger.info("要查询的组件名为：" + fullName);
        Set<OperateBean> operates = BundleManagerFace.queryOperates(fullName);
        logger.info("查询到组件中操作的数量为：" + operates.size());

        JSONObject data = new JSONObject();
        data.put("bundleName", fullName);
        data.put("operates", operates.stream().map(o -> {
            JSONObject obj = new JSONObject();
            obj.put("operateId", o.getId());
            obj.put("name", o.getName());
            obj.put("description", o.getDescription());
            obj.put("next", o.getNext());
            obj.put("class", o.getClassName());
            return obj;
        }).collect(Collectors.toList()));

        return Operate.result("200", "Success", null, data);
    }
}
