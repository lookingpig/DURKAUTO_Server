package org.lookingpig.DurkAuto.server;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.server.Model.User;
import org.lookingpig.DurkAuto.server.conf.ServerConfig;
import org.lookingpig.Tools.Ciphertext.AESCryptographService;
import org.lookingpig.Tools.Ciphertext.CryptographService;
import org.lookingpig.Tools.Service.MessageService.MessageFactory;
import org.lookingpig.Tools.Service.MessageService.MessageServiceFactory;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * WebSocket服务端
 * 
 * @author Pig
 * 
 */
@ServerEndpoint("/service/wsService")
public class WSService {
	private static final Logger logger;
	private static final AtomicInteger connectionTotal;
	private static final CryptographService cryptograph;

	private Session session;
	private StringBuffer largeMessage;
	private String aesKey;
	private User user;

	static {
		logger = LogManager.getLogger(WSService.class);
		connectionTotal = new AtomicInteger(0);
		cryptograph = new AESCryptographService();
	}

	public WSService() {
		largeMessage = new StringBuffer();
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		aesKey = AESCryptographService.generateKey();
		session.getUserProperties().put(ServerConfig.AES_KEY_KEYWORD, aesKey);

		// 累计每日连接量
		logger.info("一个连接已建立，当前连接数：" + connectionTotal.getAndIncrement());
	}

	@OnClose
	public void onClose(CloseReason cr) {
		OnLineUserPool.removeUser(user);
		
		logger.info("一个连接已关闭，原因：" + cr.getReasonPhrase());
	}

	@OnError
	public void onError(Throwable t) {
		OnLineUserPool.removeUser(user);
		logger.error("连接发生异常！", t);
	}

	@OnMessage
	public void onMessage(Session session, String message, boolean last) {
		largeMessage.append(message);

		if (last) {
			logger.info("接收到大文本：" + largeMessage.toString());
			messageComplete(largeMessage.toString());
			largeMessage.delete(0, largeMessage.length());
		}
	}

	/**
	 * 接收到一条完整的消息
	 * 
	 * @param message
	 *            消息
	 */
	private void messageComplete(String message) {
		try {
			// 校验格式
			if (!message.contains(ServerConfig.ENCRYPT_CHECK_SPLIT_MARK)) {
				logger.warn("接收到一条非法消息！message: " + message);
				return;
			}

			String[] content = message.split(ServerConfig.ENCRYPT_CHECK_SPLIT_MARK);

			if (2 != content.length) {
				logger.warn("接收到一条非法消息！message: " + message);
				return;
			}

			// 解密
			message = cryptograph.decrypt(content[0], aesKey);
			Message msg = MessageFactory.getFactory().resolve(message);
			msg.setCaller(this);
			
			// 校验是否被修改
			MessageDigest mdInst = MessageDigest.getInstance(ServerConfig.CHECK_SERVICE_ENCODE_MODE);
			mdInst.update((content[0] + msg.getSendTime()).getBytes());
			String checkStr = Base64.encodeBase64String(mdInst.digest());

			if (!content[1].equals(checkStr)) {
				logger.warn("接收到一条被修改的消息！message: " + message);
				return;
			}

			// 交由消息服务处理
			Message response = MessageServiceFactory.getFactory().getService(ServerConfig.MESSAGESERVICE_KEY_NAME).service(msg);
			
			//有响应消息时回馈客户端
			if (null != response) {
				sendMessage(response);
			}
		} catch (InvalidKeyException e) {
			logger.error("解密密匙格式不对！key: " + aesKey, e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("MD5摘要服务实例化失败！", e);
		} catch (Exception e) {
			logger.error("消息处理失败！message: " + message, e);
		}
	}

	/**
	 * 向客户端发送消息
	 * @param message 消息
	 * @throws IOException 通信异常
	 */
	public void sendMessage(Message message) throws IOException {
		String strMsg = null;

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ServerConfig.DATETIME_FORMAT);
			LocalDateTime now = LocalDateTime.now();
			
			message.setSender(ServerConfig.getConfig("durkauto.service.sender"));
			message.setSendNumber(ServerConfig.getConfig("durkauto.service.sendnumber"));
			message.setSendTime(formatter.format(now));
			
			//序列化
			strMsg = MessageFactory.getFactory().blend(message);
			
			// 加密
			strMsg = cryptograph.encrypt(strMsg, aesKey);

			// 加入校验信息
			MessageDigest mdInst;
			mdInst = MessageDigest.getInstance(ServerConfig.CHECK_SERVICE_ENCODE_MODE);
			mdInst.update((strMsg + message.getSendTime()).getBytes());
			strMsg += ServerConfig.ENCRYPT_CHECK_SPLIT_MARK + Base64.encodeBase64String(mdInst.digest());

			//发送消息
			session.getBasicRemote().sendText(strMsg);
		} catch (InvalidKeyException e) {
			logger.error("加密密匙格式不对！key: " + aesKey, e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("MD5摘要服务实例化失败！", e);
		} catch (IOException e) {
			logger.error("与客户端通信发生异常！", e);
			throw e;
		} catch (Exception e) {
			logger.error("向客户端发送消息失败！message: " + strMsg, e);
		}
	}
	
	/**
	 * 获得用户
	 * @return 用户
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置用户
	 * @param user 用户
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
