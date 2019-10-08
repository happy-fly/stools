package com.kingh.db;

import com.kingh.config.env.EnvProperties;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库工具类
 * 
 * @author 孔冠华
 *
 */
public class DbUtils {

	private static final Logger logger = LoggerFactory.getLogger(DbUtils.class);

	private static final Map<String, QueryRunner> qrs = new HashMap<>();
	private static final Map<String, DataSource> dss = new HashMap<>();
	private static final String CONFIG_FILE = EnvProperties.getEnv().getDbConfig();

	private static final Properties prop = new Properties();

	static {
		try {
			prop.load(DbUtils.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
		} catch (IOException e) {
			logger.error("读取配置文件失败。", e);
			throw new RuntimeException(e);
		}
	}

	private DbUtils() {
	}

	public static QueryRunner getInstance() {
		return getInstance("default");
	}

	public static QueryRunner getInstance(String name) {
		QueryRunner qr = qrs.get(name);
		if (qr == null) {
			synchronized (DbUtils.class) {
				if (qr == null) {
					ComboPooledDataSource ds = new ComboPooledDataSource();
					try {
						ds.setDriverClass(prop.getProperty(name + "." + "driverClass"));
						ds.setJdbcUrl(prop.getProperty(name + "." + "url"));
						ds.setUser(prop.getProperty(name + "." + "username"));
						ds.setPassword(prop.getProperty(name + "." + "password"));

						qr = new QueryRunner(ds);

						dss.put(name, ds);
						qrs.put(name, qr);
					} catch (PropertyVetoException e) {
						logger.error("获取数据库连接错误，系统具体错误信息为：" + e.getMessage(), e);
						throw new RuntimeException(e);
					}
				}
			}
		}
		return qr;
	}

	public static Map<String, QueryRunner> getCurrentQrInstance() {
		return qrs;
	}
	
	public static Map<String, DataSource> getCurrentDsInstance() {
		return dss;
	}
	
	public static void main(String[] args) {
		DbUtils.getInstance();
		System.out.println(getCurrentDsInstance());
		System.out.println(getCurrentQrInstance());
		
		DbUtils.getInstance("dev");
		System.out.println(getCurrentDsInstance());
		System.out.println(getCurrentQrInstance());
		
		DbUtils.getInstance("uat");
		System.out.println(getCurrentDsInstance());
		System.out.println(getCurrentQrInstance());
	}

}
