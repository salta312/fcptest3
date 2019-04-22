/* CustomerAccessControl.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.customer;

import java.util.UUID;

import com.fcpinsight.security.AccessType;

/**
 * Access control class to allow or deny a role access to a customer.
 *
 */
public class CustomerAccessControl {
	private UUID id;
	private UUID customerId;
	private UUID roleId;
	private AccessType accessType;

	public CustomerAccessControl() {
		id = UUID.randomUUID();
	}
	
	public CustomerAccessControl(CustomerAccessControl source) {
		id = source.id;
		customerId = source.customerId;
		accessType = source.accessType;
	}
	
	public UUID getId() {
		return id;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}

	public UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}

	@Override
	public boolean equals(Object o) { 
		if (o == this) { 
		    return true; 
		} 
	  
		if (!(o instanceof CustomerAccessControl)) { 
		    return false; 
		} 
	  
	    return id.equals(((CustomerAccessControl)o).getId());
	} 	
	
}
