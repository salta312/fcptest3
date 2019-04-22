/* Customer.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.customer;

import java.util.UUID;

import com.fcpinsight.security.User;


public class Customer {
	private UUID id;
	private String name;

	public Customer() {
		id = UUID.randomUUID();
	}
	
	public Customer(Customer sourceCustomer) {
		id = sourceCustomer.getId();
		name = sourceCustomer.getName();
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) { 
		if (o == this) { 
		    return true; 
		} 
	  
		if (!(o instanceof Customer)) { 
		    return false; 
		} 
	  
	    return id.equals(((Customer)o).getId());
	} 	
}
