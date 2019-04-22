/* User.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class User {
	private UUID id;
	private String userName;
	private String password;
	List<Role> roles = new ArrayList<Role>();

	public User() {
		id = UUID.randomUUID();
	}
	
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public User(User sourceUser) {
		id = sourceUser.getId();
		userName = sourceUser.getUserName();
		password = sourceUser.getPassword();
	}
	
	/**
	 * Returns an unmodifiable list of roles
	 * @return
	 */
	public List<Role> getRoles() {
	    return Collections.unmodifiableList(roles); 
	}
	
	/**
	 * Adds the user to the role.
	 * If the user already exists it will be ignored.  
	 * @param user
	 * @return
	 */
	public void addRole(Role role) {
		assert role != null;
		
		if (!this.hasRole(role)) {
			roles.add(role);
		}
	}

	/**
	 * Return true if the user has the role. 
	 * @param user
	 * @return
	 */
	public boolean hasRole(Role role) {
		assert role != null;
		
		return roles.stream().anyMatch(x -> role.getId().equals(x.getId()));
	}

	public UUID getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	} 
	
	@Override
	public boolean equals(Object o) { 
		if (o == this) { 
		    return true; 
		} 
	  
		if (!(o instanceof User)) { 
		    return false; 
		} 
	  
	    return id.equals(((User)o).getId());
	} 	
}
