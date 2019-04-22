/* Role.java 
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.security;

import java.util.UUID;

/** 
 * A role that a user may have. 
 * In a real system roles would have privileges and there would be a way to see the members of the role. 
 *
 */
public class Role {
	private UUID id;
	String name;
	
	public Role() {
		id = UUID.randomUUID();
	}
	
	public Role(String name) {
		this.name = name;
	}
	
	public Role(Role sourceRole) {
		id = sourceRole.id;
		name = sourceRole.name;
	}
	

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}