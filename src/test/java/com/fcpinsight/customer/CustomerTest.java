/* CustomerTest.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */


package com.fcpinsight.customer;

import com.fcpinsight.security.AccessType;
import com.fcpinsight.security.Role;
import com.fcpinsight.security.SecurityService;
import com.fcpinsight.security.Session;
import com.fcpinsight.system.SystemException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;



public class CustomerTest {
	private static final String CUSTOMER1_NAME = "customer1";
	private static final String CUSTOMER2_NAME = "customer2";
	
	private static final String ROLE1_NAME = "role1";
	private static final String ROLE2_NAME = "role2";
	private static final String ROLE3_NAME = "role3";
	

	
	@Test
	public void testEquality() throws SystemException {
		// test Customer
		Customer customer1 = new Customer();
		
		Customer customerTest = new Customer(customer1);
		assertEquals(customer1, customerTest);
		
		// test CustomerAccessControl
		CustomerAccessControl customerAccessControl1 = new CustomerAccessControl();
		
		CustomerAccessControl customerAccessControlTest = new CustomerAccessControl(customerAccessControl1);
		assertEquals(customerAccessControl1, customerAccessControlTest);
		
	}
	
	@Test
	public void testCustomerReadWrite() throws SystemException {
		SecurityService securityService = new SecurityService();
		
		Session session = securityService.authenticate("admin", "password");
		assertTrue(session != null);
		
		CustomerService customerService = new CustomerService();
		
		Customer customer1 = new Customer();
		customer1.setName(CUSTOMER1_NAME);
		customerService.customerSave(session, customer1);
		
		Customer customer2 = new Customer();
		customer2.setName(CUSTOMER2_NAME);
		customerService.customerSave(session, customer2);

		
		
		
		List<Customer> customerList = customerService.customerList(session);
		assertEquals(2, customerList.size());

		
		
		Customer customer = customerService.customerFind(session, customer1.getId());
		assertTrue(customer != null);
		assertEquals(CUSTOMER1_NAME, customer1.getName());
		
	}
	
	@Test
	public void testCustomerAccessControReadWrite() throws SystemException {
		SecurityService securityService = new SecurityService();
		
		Session session = securityService.authenticate("admin", "password");
		assertTrue(session != null);
		
		CustomerService customerService = new CustomerService();

		// create customers
		Customer customer1 = new Customer();
		customer1.setName(CUSTOMER1_NAME);
		customerService.customerSave(session, customer1);
		
		Customer customer2 = new Customer();
		customer2.setName(CUSTOMER2_NAME);
		customerService.customerSave(session, customer2);
		
		// creata roles
		Role role1 = new Role();
		role1.setName(ROLE1_NAME);
		securityService.saveRole(session, role1);
		
		Role role2 = new Role();
		role2.setName(ROLE2_NAME);
		securityService.saveRole(session, role2);
		
		Role role3 = new Role();
		role3.setName(ROLE3_NAME);
		securityService.saveRole(session, role3);
		
		
		// create access records
		CustomerAccessControl customerAccesControl1 = new CustomerAccessControl();
		customerAccesControl1.setCustomerId(customer1.getId());
		customerAccesControl1.setRoleId(role1.getId());
		customerAccesControl1.setAccessType(AccessType.ALOWED);
		customerService.customerAccessControlSave(session, customerAccesControl1);
		
		CustomerAccessControl customerAccesControl2 = new CustomerAccessControl();
		customerAccesControl2.setCustomerId(customer1.getId());
		customerAccesControl2.setRoleId(role2.getId());
		customerAccesControl2.setAccessType(AccessType.DENIED);
		customerService.customerAccessControlSave(session, customerAccesControl2);
		
		CustomerAccessControl customerAccesControl3 = new CustomerAccessControl();
		customerAccesControl3.setCustomerId(customer2.getId());
		customerAccesControl3.setRoleId(role3.getId());
		customerAccesControl3.setAccessType(AccessType.ALOWED);
		customerService.customerAccessControlSave(session, customerAccesControl3);
		
		// read list of access records
		List<CustomerAccessControl> customerAccessControlList = customerService.customerAccessControlList(session);
		assertEquals(3, customerAccessControlList.size());
		assertTrue(customerAccessControlList.contains(customerAccesControl1));
		assertTrue(customerAccessControlList.contains(customerAccesControl2));
		assertTrue(customerAccessControlList.contains(customerAccesControl3));


		
	}
	

}
