package org.lookingpig.DurkAuto.server.conf;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerConfig {

	/**
	 * 日期时间格式化格式
	 */
	public static final String DATETIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
	
	/**
	 * 消息服务索引名称
	 */
	public static final String MESSAGESERVICE_KEY_NAME = "ServiceName";
	
	/**
	 * 消息服务消息类型
	 */
	public static final String MESSAGESERVICE_TYPE = "MessageType";
	
	/**
	 * 消息服务消息类型-请求
	 */
	public static final String MESSAGESERVICE_TYPE_REQUEST = "Request";
	
	/**
	 * 消息服务消息类型-响应
	 */
	public static final String MESSAGESERVICE_TYPE_RESPONSE = "Response";

	/**
	 * 密文与摘要分割符
	 */
	public static final String ENCRYPT_CHECK_SPLIT_MARK = "&";

	/**
	 * 摘要算法模式
	 */
	public static final String CHECK_SERVICE_ENCODE_MODE = "MD5";

	private static final String SERVER_CONFIG_PATH = "/server_config.properties";

	private static final Logger logger;
	private static final Properties conf;

	static {
		logger = LogManager.getLogger(ServerConfig.class);
		conf = new Properties();

		try {
			logger.info("开始加载服务器配置文件。");
			conf.load(ServerConfig.class.getClassLoader().getResourceAsStream(SERVER_CONFIG_PATH));
			logger.info("加载服务器配置文件完成。");
		} catch (IOException e) {
			logger.error("加载服务器配置文件失败！path: " + Class.class.getResource("/").getPath(), e);
		}
	}
	
	/**
	 * 获得指定配置信息
	 * @param key 索引
	 * @return 配置信息
	 */
	public static String getConfig(String key) {
		return conf.getProperty(key);
	}
}
