/* CustomerService.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.customer;

import com.fcpinsight.system.SystemException;

import java.util.List;
import java.util.UUID;
import com.fcpinsight.security.Session;

/**
 * Service to manage customers
 */
public final class CustomerService {
	
	
	
	public void customerSave(Session session, Customer customer) throws SystemException {
		session.getDb().customerSave(customer);		
	}
	
	/**
	 * Returns a list of customers. 
	 * TODO: Throw an exceptions if access control prevents the current user from accessing this customer.
	 */
	public Customer customerFind(Session session, UUID customerId) throws SystemException {
		return session.getDb().customerFind(customerId);
	}
	
	/**
	 * Returns a list of customers. 
	 * TODO: Filter the list using access control for the user that owns the session.  
	 */
	public List<Customer> customerList(Session session) throws SystemException {
		return session.getDb().customerList();
	}
	
	
	public void customerAccessControlSave(Session session, CustomerAccessControl customerAccessControl) throws SystemException {
		session.getDb().customerAccessControlSave(customerAccessControl);		
	}
	
	public CustomerAccessControl customerAccessControlFind(Session session, UUID customerAccessControlId) throws SystemException {
		return session.getDb().customerAccessControlFind(customerAccessControlId);
	}
	
	public List<CustomerAccessControl> customerAccessControlList(Session session) throws SystemException {
		return session.getDb().customerAccessControlList();
	}
	
	
	
	
	

}
