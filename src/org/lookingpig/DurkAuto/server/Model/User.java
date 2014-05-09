package org.lookingpig.DurkAuto.server.Model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.server.WSService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 用户模型
 * 
 * @author Pig
 * 
 */
public class User {
	private static final Logger logger = LogManager.getLogger(User.class);

	private Integer id;
	private String userName;
	private WSService service;

	public User() {

	}

	/**
	 * 构造方法
	 * 
	 * @param id
	 *            标识
	 * @param service
	 *            会话
	 */
	public User(String userName, WSService service) {
		this.userName = userName;
		this.service = service;
	}

	/**
	 * 向用户发送消息
	 * 
	 * @param message
	 *            消息
	 */
	public void sendMessage(Message message) {
		try {
			service.sendMessage(message);
		} catch (Exception e) {
			logger.error("向客户端发送消息失败！username: " + userName + ", message: " + message, e);
		}
	}

	/**
	 * 获得用户标识
	 * 
	 * @return 标识
	 */
	public Integer getID() {
		return id;
	}

	/**
	 * 设置用户标识
	 * 
	 * @param id
	 *            标识
	 */
	public void setID(Integer id) {
		this.id = id;
	}

	/**
	 * 设置WebSocket服务
	 * 
	 * @param service
	 *            WebSocket服务
	 */
	public void setService(WSService service) {
		this.service = service;
	}

	/**
	 * 获得用户名
	 * 
	 * @return 用户名
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置用户名
	 * 
	 * @param userName
	 *            用户名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
