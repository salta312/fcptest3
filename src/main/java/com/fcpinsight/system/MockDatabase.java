/* MockDatabase.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fcpinsight.customer.Customer;
import com.fcpinsight.customer.CustomerAccessControl;
import com.fcpinsight.security.Role;
import com.fcpinsight.security.User;

/*
 * This mocks what a database system might do.
 * Only the service code should call this although there is no way to enforce this in Java8. 
 */

public class MockDatabase {
	Map<UUID, User> users = new HashMap<UUID, User>();
	// Map<UUID, Role> roles = new HashMap<UUID, Role>();
	Map<UUID, Customer> customers = new HashMap<UUID, Customer>();
	Map<UUID, CustomerAccessControl> customerAccessControls = new HashMap<UUID, CustomerAccessControl>();
	
	/*
	 * Default constructor defined at package scope. 
	 * The only way for a class in another package to get one is to use DefaultDatabaseBuilder. 
	 */
	MockDatabase() {
		
	}
	
	/*
	 * User CRUD
	 */
	public User userFind(UUID userId) {
		if (userId == null) {
			return null;
		}
		User user = users.get(userId);
		return user == null ? null : new User(user); 
	}
	
	public User userFindByUserName(String name) {
		if (name == null) {
			return null;
		}
		User user = users.values().stream().filter(x -> name.equals(x.getUserName())).findFirst().get();
		return user == null ? null : new User(user); 
	}
	
	public List<User> userList() {
		return users.values().stream().collect(Collectors.toList());
	}
	
	public void userSave(User user) {
		if (user == null) throw new RuntimeException("cannot update null user");

		users.put(user.getId(), user);  
	}
	
	public void userDelete(User user) {
		if (user == null) throw new RuntimeException("cannot remove null user");

		users.remove(user.getId());  
	}

	
	/*
	 * Customer CRUD
	 */
	public Customer customerFind(UUID customerId) {
		Customer customer =  customers.get(customerId);
		return customer == null ? null : new Customer(customer);
	}
	
	public Customer customerFindByName(String name) {
		if (name == null) {
			return null;
		}
		return customers.values().stream().filter(x -> name.equals(x.getName())).findFirst().get();  
	}
	
	public List<Customer> customerList() {
		return customers.values().stream().collect(Collectors.toList());
	}
	
	public void customerSave(Customer customer) {
		if (customer == null) throw new RuntimeException("cannot update null customer");

		customers.put(customer.getId(), customer);  
	}
	
	public void customerDelete(Customer customer) {
		if (customer == null) throw new RuntimeException("cannot remove null customer");

		customers.remove(customer.getId());  
	}

	/*
	 * CustomerAccessControl CRUD
	 */
	public CustomerAccessControl customerAccessControlFind(UUID customerAccessControlId) {
		CustomerAccessControl customerAccessControl =  customerAccessControls.get(customerAccessControlId);
		return customerAccessControl == null ? null : new CustomerAccessControl(customerAccessControl);
	}
	
	/*
	 * Returns a list of customerAccessControl Records. 
	 * Records are grouped by Organization 
	 * 
	 */
	public List<CustomerAccessControl> customerAccessControlList() {
		return customerAccessControls.values().stream()
				.sorted((x, y) -> x.getCustomerId().compareTo(y.getCustomerId()))
				.collect(Collectors.toList());
	}
	
	public void customerAccessControlSave(CustomerAccessControl customerAccessControl) {
		if (customerAccessControl == null) throw new RuntimeException("cannot update null customerAccessControl");

		customerAccessControls.put(customerAccessControl.getId(), customerAccessControl);  
	}
	
	public void customerAccessControlDelete(CustomerAccessControl customerAccessControl) {
		if (customerAccessControl == null) throw new RuntimeException("cannot remove null customerAccessControl");

		customerAccessControls.remove(customerAccessControl.getId());  
	}

	
	
	


}
