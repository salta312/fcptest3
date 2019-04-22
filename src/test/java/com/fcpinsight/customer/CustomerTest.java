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
	private static final String CUSTOMER3_NAME = "customer3";
	private static final String CUSTOMER4_NAME = "customer4";
	private static final String CUSTOMER5_NAME = "customer5";
	private static final String CUSTOMER6_NAME = "customer6";
	private static final String CUSTOMER7_NAME = "customer7";
	private static final String CUSTOMER8_NAME = "customer8";
	
	private static final String ROLE1_NAME = "role1";
	private static final String ROLE2_NAME = "role2";
	private static final String ROLE3_NAME = "role3";
	private static final String ROLE4_NAME = "role4";
	

	
	@Test
	public void testEquality() throws SystemException {
		SecurityService securityService = new SecurityService();
		
		Session session = securityService.authenticate("admin", "password");
		assertTrue(session != null);
		// test Customer
		Customer customer1 = createCustomer(session, CUSTOMER1_NAME);
		
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
		
		Customer customer1 = createCustomer(session, CUSTOMER1_NAME);
		Customer customer2 = createCustomer(session, CUSTOMER2_NAME);

		
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
		Customer customer1 = createCustomer(session, CUSTOMER1_NAME);
		Customer customer2 = createCustomer(session, CUSTOMER2_NAME);

		
		// creata roles
		Role role1 = createRole(session, ROLE1_NAME);
		Role role2 = createRole(session, ROLE2_NAME);
		Role role3 = createRole(session, ROLE3_NAME);
		
		// create access records
		CustomerAccessControl customerAccesControl1 = addCustomerAccessControl(session, customer1, role1, AccessType.ALLOW);
		CustomerAccessControl customerAccesControl2 = addCustomerAccessControl(session, customer1, role2, AccessType.DENY);
		CustomerAccessControl customerAccesControl3 = addCustomerAccessControl(session, customer2, role3, AccessType.ALLOW);
		
		
		// read list of access records
		List<CustomerAccessControl> customerAccessControlList = customerService.customerAccessControlList(session);
		assertEquals(3, customerAccessControlList.size());
		assertTrue(customerAccessControlList.contains(customerAccesControl1));
		assertTrue(customerAccessControlList.contains(customerAccesControl2));
		assertTrue(customerAccessControlList.contains(customerAccesControl3));

	}
	

	private Role createRole(Session session, String roleName) throws SystemException {
		// SecurityService securityService = new SecurityService();
		
		Role role = new Role();
		role.setName(roleName);
		// securityService.saveRole(session, role);
		
		return role;
	}
	
	private Customer createCustomer(Session session, String customerName) throws SystemException {
		CustomerService customerService = new CustomerService();

		Customer customer = new Customer();
		customer.setName(CUSTOMER1_NAME);
		customerService.customerSave(session, customer);
		
		return customer;
	}

	private CustomerAccessControl addCustomerAccessControl(Session session, Customer customer, Role role, AccessType accessType) throws SystemException {
		CustomerService customerService = new CustomerService();

		CustomerAccessControl customerAccessControl = new CustomerAccessControl();
		customerAccessControl.setCustomerId(customer.getId());
		customerAccessControl.setRoleId(role.getId());
		customerAccessControl.setAccessType(accessType);
		customerService.customerAccessControlSave(session, customerAccessControl);
		
		return customerAccessControl;
	}
	

	

}
