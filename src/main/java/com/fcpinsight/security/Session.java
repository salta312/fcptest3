/* Session.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.security;

import com.fcpinsight.system.MockDatabase;

/*
 * Session needed to make service calls in our system .
 * 
 */
public class Session {
	private final MockDatabase db;
	private final User user; 
	
	public Session(MockDatabase db, User user) {
		this.db = db;
		this.user = user;
	}

	public MockDatabase getDb() {
		return db;
	}

	public User getUser() {
		return user;
	}

}
