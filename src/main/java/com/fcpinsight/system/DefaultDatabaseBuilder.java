/* DefaultDatabaseBuilder.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */
package com.fcpinsight.system;

import com.fcpinsight.security.Role;
import com.fcpinsight.security.User;

public class DefaultDatabaseBuilder {

	public static MockDatabase buildDatabase() {
		MockDatabase database = new MockDatabase();

		User user1 = new User("admin", "password"); 
		database.userSave(user1);

		return database;
	}
				
				
		
}
