/* SecurityService.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.security;

import com.fcpinsight.system.MockDatabase;
import com.fcpinsight.system.SystemException;

import java.util.List;
import java.util.UUID;

import com.fcpinsight.system.DefaultDatabaseBuilder;
import com.fcpinsight.system.InvalidSessionException;

/**
 * Service to manage users and roles and to authenticate users
 */
public final class SecurityService {
	
	
	public SecurityService() {
	}
	
	public Session authenticate(String userName, String password) {
		if (userName == null || password == null) {
			return null;
		}
		
		MockDatabase db = DefaultDatabaseBuilder.buildDatabase();
		
		User user = db.userFindByUserName(userName); 
		if (user != null && password.equals(user.getPassword())) {
			return new Session(db, user);
		}
		return null;
	}
	
	
	public User newUser(Session session) {
		if (session == null) return null;

		return new User();
	}
	
	public void saveUser(Session session, User user) throws SystemException {
		if (session == null) throw new InvalidSessionException("Session cannot be null");

		session.getDb().userSave(user);;
	}
	
	public User userFind(Session session, UUID userId) throws SystemException {
		return session.getDb().userFind(userId);
	}
	
	public List<User> userList(Session session) throws SystemException {
		return session.getDb().userList();
	}
	

}
