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
		id = sourceUser.id;
		userName = sourceUser.userName;
		password = sourceUser.password;
		roles = sourceUser.roles;
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
		if (role == null) throw new RuntimeException("cannot add null role");
		
		if (!this.hasRole(role.getId())) {
			roles.add(role);
		}
	}

	/**
	 * Return true if the user has the role. 
	 * @param user
	 * @return
	 */
	public boolean hasRole(UUID roleId) {
		if (roleId == null) throw new RuntimeException("cannot have null roldeId");
		
		return roles.stream().anyMatch(x -> roleId.equals(x.getId()));
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
