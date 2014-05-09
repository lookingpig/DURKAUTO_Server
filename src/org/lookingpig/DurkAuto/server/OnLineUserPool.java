package org.lookingpig.DurkAuto.server;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lookingpig.DurkAuto.server.Model.User;

/**
 * 在线用户池
 * @author Pig
 *
 */
public class OnLineUserPool {
	private static final Map<String, User> users = new ConcurrentHashMap<>(); 
	
	/**
	 * 添加一个在线用户
	 * @param user 用户
	 */
	public static void addUser(User user) {
		users.put(user.getUserName(), user);
	}
	
	/**
	 * 获得所有在线用户
	 * @return 在线用户
	 */
	public static Collection<User> getUsers() {
		return Collections.unmodifiableCollection(users.values());
	}
	
	/**
	 * 删除一个在线用户
	 * @param user 用户
	 */
	public static void removeUser(User user) {
		users.remove(user.getUserName());
	}
}
