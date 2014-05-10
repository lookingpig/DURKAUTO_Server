package org.lookingpig.DurkAuto.server.conf;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerConfig {

	/**
	 * 消息服务索引名称
	 */
	public static final String MESSAGESERVICE_KEY_NAME = "ServiceName";

	/**
	 * 密文与摘要分割符
	 */
	public static final String ENCRYPT_CHECK_SPLIT_MARK = "&";

	/**
	 * 摘要算法模式
	 */
	public static final String CHECK_SERVICE_ENCODE_MODE = "MD5";

	/**
	 * AES密匙关键字
	 */
	public static final String AES_KEY_KEYWORD = "aeskey";

	private static final String SERVER_CONFIG_PATH = "/server_config.properties";

	private static final Logger logger;
	private static final Properties conf;

	static {
		logger = LogManager.getLogger(ServerConfig.class);
		conf = new Properties();

		try {
			conf.load(Class.class.getResourceAsStream(SERVER_CONFIG_PATH));
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
