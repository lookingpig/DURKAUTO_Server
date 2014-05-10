package org.lookingpig.DurkAuto.server.message.service;

import org.lookingpig.DurkAuto.server.OnLineUserPool;
import org.lookingpig.DurkAuto.server.WSService;
import org.lookingpig.DurkAuto.server.Model.User;
import org.lookingpig.DurkAuto.server.conf.ServerConfig;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 用户上线服务
 * @author Pig
 *
 */
public class OnLineService implements MessageService {

	@Override
	public Message service(Message message) {
		WSService service = (WSService) message.getCaller();
		User user = new User(message.getSender(), service);
		OnLineUserPool.addUser(user);
		
		Message response = new Message();
		response.setSender(ServerConfig.getConfig("durkauto.service.sender"));
		response.setSendNumber(ServerConfig.getConfig("durkauto.service.sendnumber"));
		return null;
	}

}
