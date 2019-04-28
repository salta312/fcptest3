package com.fcpinsight.customer;

import com.fcpinsight.security.AccessType;
import com.fcpinsight.security.Role;
import com.fcpinsight.security.SecurityService;
import com.fcpinsight.security.Session;
import com.fcpinsight.security.User;
import com.fcpinsight.system.SystemException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fa;

import java.util.List;

import org.junit.Test;

public class CustomerServiceTest {

	private static final String ROLE1 = "1";
	private static final String ROLE2 = "2";
	private static final String ROLE3 = "3";
	
	
	@Test(expected=SystemException.class)
	public void testCustomerFind() throws SystemException {
		SecurityService securityService = new SecurityService();
		
		Session session = securityService.authenticate("admin", "password");
		assertTrue(session != null);
		
		Role role1 = new Role();
		role1.setName(ROLE1);		
		User user4 = session.getUser();
		user4.addRole(role1);
		securityService.saveUser(session, user4);
		assertTrue(session != null);
		
		Role role2 = new Role();
		role2.setName(ROLE2);
		Role role3 = new Role();
		role3.setName(ROLE3);
		
		Customer customer1 = createCustomer(session, "CUSTOMER1");
		Customer customer2 = createCustomer(session, "CUSTOMER1");
		Customer customer3 = createCustomer(session, "CUSTOMER1");
		CustomerService customerService = new CustomerService();
		
		//customer and user share the same role and allow accessType	
		addCustomerAccessControl(session, customer1, role1, AccessType.ALLOW);
		Customer customer4 = customerService.customerFind(session, customer1.getId());
		assertEquals(customer4.getId(), customer1.getId());
		
		//Tests a customer without any customerAccessControl
		Customer customer5 = customerService.customerFind(session, customer3.getId());
		assertEquals(customer5.getId(), customer3.getId());
		
		//Tests a customer that has Deny customerAccessControl to another role
		addCustomerAccessControl(session, customer2, role2, AccessType.DENY);
		Customer customer6 = customerService.customerFind(session, customer2.getId());
		assertEquals(customer6.getId(), customer2.getId());
		
		// starting from junit5 where it has assertThrows to test for SystemException
		Customer customer7 = createCustomer(session, "CUSTOMER1");
		addCustomerAccessControl(session, customer7, role1, AccessType.DENY);
		customerService.customerFind(session, customer7.getId());	
	}
	
	@Test
	public void testCustomerList() throws SystemException {
		SecurityService securityService = new SecurityService();
		
		Session session = securityService.authenticate("admin", "password");
		assertTrue(session != null);
		
		Role role1 = new Role();
		role1.setName(ROLE1);
		Role role4 = new Role();
		role4.setName("4");
		
		User user4 = session.getUser();
		user4.addRole(role1);
		user4.addRole(role4);
		securityService.saveUser(session, user4);
		assertTrue(session != null);
		
		Customer customer1 = createCustomer(session, "CUSTOMER1");
		
		//Test a customer without AccessControl
		CustomerService customerService = new CustomerService();		
		List<Customer> customers = customerService.customerList(session);
		assertTrue(customers.size() == 1);
		
		//customer with Deny accessType to a role that is not in the list of the user
		Role role2 = new Role();
		role2.setName(ROLE2);
		Customer customer2 = createCustomer(session, "CUSTOMER2");
		addCustomerAccessControl(session, customer2, role2, AccessType.DENY);
		customers  = customerService.customerList(session);
		assertTrue(customers.size() == 2);
			
		//allow AccessType to user's role
		Customer customer3 = createCustomer(session, "CUSTOMER3");
		addCustomerAccessControl(session, customer3, role1, AccessType.ALLOW);
		customers  = customerService.customerList(session);
		assertTrue(customers.size() == 3);
		
		//allow and deny to user's role
		Customer customer4 = createCustomer(session, "CUSTOMER4");
		addCustomerAccessControl(session, customer4, role1, AccessType.ALLOW);
		addCustomerAccessControl(session, customer4, role4, AccessType.DENY);
		customers  = customerService.customerList(session);
		assertTrue(customers.size() == 3);
		
		//allow access type to another user
		Customer customer5 = createCustomer(session, "CUSTOMER5");
		addCustomerAccessControl(session, customer5, role2, AccessType.ALLOW);
		customers  = customerService.customerList(session);
		assertTrue(customers.size() == 3);
	}
	
	
	private Customer createCustomer(Session session, String customerName) throws SystemException {

		CustomerService customerService = new CustomerService();

		Customer customer = new Customer();
		customer.setName(customerName);
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
