package org.lookingpig.DurkAuto.server.init;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.server.conf.ServerConfig;
import org.lookingpig.Tools.Service.MessageService.MessageServiceFactory;

/**
 * 用于初始化
 * 
 * @author Pig
 * 
 */

public class InitSysServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(InitSysServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitSysServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.info("----------开始初始化DurkAuto服务器服务----------");
		
		try {
			logger.info("1，初始化消息服务");			
			File msgSerCof = new File(InitSysServlet.class.getClassLoader().getResource("/").getPath()
					+ ServerConfig.getConfig("durkauto.service.messageservice.config.path"));
			
			MessageServiceFactory.getFactory().loadServices(msgSerCof);
		} catch (Exception e) {
			logger.error("初始化DurkAuto服务器失败！原因：", e);
		}
		
		logger.info("----------成功初始化DurkAuto服务器服务----------");
	}
}
