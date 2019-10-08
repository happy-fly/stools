package com.kingh.core.api;

import com.alibaba.fastjson.JSONObject;
import com.kingh.db.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;

/**
 * 带事务的操作
 */
public abstract class AbstractTransactionOperate implements Operate {

    public boolean isTx() {
        return true;
    }

    public String getDataSource() {
        return "default";
    }

    @Override
    public JSONObject start(JSONObject params) throws Exception {
        if (isTx()) {
            QueryRunner qr = DbUtils.getInstance(getDataSource());
            Connection conn = qr.getDataSource().getConnection();
            conn.setAutoCommit(false);
            try {
                JSONObject result = run(conn, params);
                conn.commit();
                return result;
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                conn.close();
            }
        }

        return run(params);
    }

    public JSONObject run(JSONObject params) throws Exception {
        throw new UnsupportedOperationException();
    }

    public JSONObject run(Connection conn, JSONObject params) throws Exception {
        throw new UnsupportedOperationException();
    }
}
