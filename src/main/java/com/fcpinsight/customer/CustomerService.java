/* CustomerService.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.customer;

import com.fcpinsight.system.SystemException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fcpinsight.security.AccessType;
import com.fcpinsight.security.Role;
import com.fcpinsight.security.Session;
import com.fcpinsight.security.User;

/**
 * Service to manage customers
 */
public final class CustomerService {
	
	private Map<UUID, String> customerAccessTypeHashMap;
	private Map<UUID, HashMap<UUID, AccessType>> userToRoleToAccessTypeMap;
	private Set<UUID> allowCustomerAccessType;
	private Set<UUID> denyCustomerAccessType;
	private Set<UUID> mixedCustomerAccessType;
	
	public CustomerService() {
		
	}
	
	public void customerSave(Session session, Customer customer) throws SystemException {
		session.getDb().customerSave(customer);		
	}
		
	/**
	 * Returns a list of customers. 
	 * TODO: Throw an exceptions if access control prevents the current user from accessing this customer.
	 */
	public Customer customerFind(Session session, UUID customerId) throws SystemException {
		List<CustomerAccessControl> accessControls = session.getDb().customerAccessControlList();
		Map<UUID, AccessType> accessControlsMap = getAccessControlsForACustomer(customerId, accessControls);
		if (accessControlsMap.size() == 0) {
			return session.getDb().customerFind(customerId);
		} else {
			if (isPermittedToAccessACustomer(accessControlsMap, session.getUser())) {
				return session.getDb().customerFind(customerId);
			}
			throw new SystemException("user with username "+ session.getUser().getUserName() + "is not allowed to access the customer with an id "+ customerId.toString());
		}	
	}	
	
	/**
	 * generates a map that links rolesIds to AccessType from CustomerAccessControl links
	 * for a customer with customerId
	 * 
	 * @param customerId
	 * @param accessControls
	 * @return Map
	 */
	private Map<UUID, AccessType> getAccessControlsForACustomer(UUID customerId, List<CustomerAccessControl> accessControls) {
		Map<UUID, AccessType> map = new HashMap<UUID, AccessType>();
		for(CustomerAccessControl accessControl: accessControls) {
			if(accessControl.getCustomerId() == customerId) {
				if(!map.containsKey(accessControl.getRoleId())) {
					map.put(accessControl.getRoleId(), accessControl.getAccessType());
				} else {
					if(map.get(accessControl.getRoleId()) != accessControl.getAccessType() && accessControl.getAccessType() == AccessType.DENY) {
						map.put(accessControl.getRoleId(), accessControl.getAccessType());
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * returns whether the user (from session) can access a particular customer or not 
	 * @param accessControlMap
	 * @param user
	 * @return boolean
	 */
	private boolean isPermittedToAccessACustomer(Map<UUID, AccessType> accessControlMap, User user) {
		AccessType accessType;
		boolean isAllowed = false;
		for(Role role: user.getRoles()) {
			if(accessControlMap.containsKey(role.getId())) {
				accessType = accessControlMap.get(role.getId());
				if(accessType == AccessType.DENY)
					return false;
				else 
					isAllowed = true;
			} 
		}
		if (!isAllowed) {
			for(AccessType access: accessControlMap.values()) {
				if(access == AccessType.ALLOW) {
					return false;
				}
			}
			return true;
		}
		return isAllowed;
	}

	
	/**
	 * Returns a list of customers. 
	 * TODO: Filter the list using access control for the user that owns the session.  
	 */
	public List<Customer> customerList(Session session) throws SystemException {
		List<CustomerAccessControl> list = session.getDb().customerAccessControlList();
		setCustomerAccessTypeHashMap(list);
		devideCustomersByAccessTypeCategories();
		List<UUID> customers = new ArrayList<UUID>();
		customers.addAll(getAllowedCustomersIds(AccessType.ALLOW.name(), session));
		customers.addAll(getAllowedCustomersIds(AccessType.DENY.name(), session));
		customers.addAll(getAllowedCustomersIds("mixed", session));
		List<Customer> customerList = getCustomersFromUUID(session, customers);
		customerList.addAll(getCustomersWithoutAccessControl(session));
		return customerList;
	}
	

	/**
	 * this method is designed to take all the CustomerAccessControls and
	 * divide them into 3 categories: Customers with all Deny accessTypes,
	 * Customers with all Allow accessTypes and mixed accessTypes
	 * then it stores in a map customerId with a category for further processing
	 * and second map hashes customerIds to a map of roles of this Customer and his/her AllowTypes
	 * @param customerAccessControlList
	 */
	private void setCustomerAccessTypeHashMap(List<CustomerAccessControl> customerAccessControlList) {
		Map<UUID, String> customerAccessTypeHashMap = new HashMap<UUID, String>();
		Map<UUID, HashMap<UUID, AccessType>> userToRoleToAccessTypeMap = new HashMap<UUID, HashMap<UUID, AccessType>>();
		for(CustomerAccessControl customerAccessControl: customerAccessControlList) {
			if(customerAccessTypeHashMap.containsKey(customerAccessControl.getCustomerId())) {
				if(customerAccessTypeHashMap.get(customerAccessControl.getCustomerId()) != "mixed" && customerAccessTypeHashMap.get(customerAccessControl.getCustomerId()) != customerAccessControl.getAccessType().name()) {
					customerAccessTypeHashMap.put(customerAccessControl.getCustomerId(), "mixed");
				} 
				userToRoleToAccessTypeMap.get(customerAccessControl.getCustomerId()).put(customerAccessControl.getRoleId(), customerAccessControl.getAccessType());
			} else {
				customerAccessTypeHashMap.put(customerAccessControl.getCustomerId(), customerAccessControl.getAccessType().name());
				userToRoleToAccessTypeMap.put(customerAccessControl.getCustomerId(), new HashMap<UUID, AccessType>());
				userToRoleToAccessTypeMap.get(customerAccessControl.getCustomerId()).put(customerAccessControl.getRoleId(), customerAccessControl.getAccessType());
			}
		}
		this.customerAccessTypeHashMap = customerAccessTypeHashMap;
		this.userToRoleToAccessTypeMap = userToRoleToAccessTypeMap;
	}
	
	/**
	 * Separates customer into 3 categories: Customers with all Deny accessTypes,
	 * Customers with all Allow accessTypes and mixed accessTypes and writes them
	 * down in different sets for further processing
	 */
	private void devideCustomersByAccessTypeCategories() {
		Set<UUID> allowCustomerAccessType = new HashSet<UUID>();
		Set<UUID> denyCustomerAccessType = new HashSet<UUID>();
		Set<UUID> mixedCustomerAccessType = new HashSet<UUID>();
		for (Map.Entry<UUID, String> entry : customerAccessTypeHashMap.entrySet()) {
			if(entry.getValue() == AccessType.ALLOW.name()) {
				allowCustomerAccessType.add(entry.getKey());
			} else if (entry.getValue() == AccessType.DENY.name()) {
				denyCustomerAccessType.add(entry.getKey());
			} else {
				mixedCustomerAccessType.add(entry.getKey());
			}
		}
		this.allowCustomerAccessType = allowCustomerAccessType;
		this.denyCustomerAccessType = denyCustomerAccessType;
		this.mixedCustomerAccessType = mixedCustomerAccessType;
	}
	
	/**
	 * calls a function to receive a list of ids of customers depending on their 
	 * category: allow, deny, mixed
	 * @param operation
	 * @param session
	 * @return List
	 */
	private List<UUID> getAllowedCustomersIds(String operation, Session session) {
		switch (operation) {
			case "ALLOW":
				return addAllCustomersWithMonoType(session.getUser(), allowCustomerAccessType, false);
			case "DENY":
				return addAllCustomersWithMonoType(session.getUser(), denyCustomerAccessType, true);
			default:
				return addAllCustomersWithMixedType(session.getUser(), mixedCustomerAccessType);
		}
	}
	
	/**
	 * generates a list of customers ids that can be accessed by current user
	 * from set of customers with all allow accessTypes or all deny accessType
	 * @param user
	 * @param set
	 * @param isUserAdded
	 * @return List
	 */
	private List<UUID> addAllCustomersWithMonoType(User user, Set<UUID> set, boolean isUserAdded) {
		List<UUID> customers = new ArrayList<UUID>();
		boolean addUser;
		for(UUID customerId: set) {
			if(userToRoleToAccessTypeMap.containsKey(customerId)) {
				addUser = isUserAdded;
				for(Role role: user.getRoles()) {
					if(userToRoleToAccessTypeMap.get(customerId).containsKey(role.getId())) {
						addUser = !addUser;
						break;
					}
				}
				if(addUser) {
					customers.add(customerId);
				}
			}
		}
		return customers;
	}
	
	/**
	 * generates a list of customers ids that can be accessed by current user
	 * from set of customers with allow accessTypes and deny accessType
	 * @param user
	 * @param set
	 * @return List
	 */
	private List<UUID> addAllCustomersWithMixedType(User user, Set<UUID> set) {
		List<UUID> customers = new ArrayList<UUID>();
		boolean addUser;
		for(UUID customerId: set) {
			if(userToRoleToAccessTypeMap.containsKey(customerId)) {
				addUser = false;
				for(Role role: user.getRoles()) {
					if(userToRoleToAccessTypeMap.get(customerId).containsKey(role.getId())) {
						if(userToRoleToAccessTypeMap.get(customerId).get(role.getId()) == AccessType.ALLOW) {
							addUser = true;
						} else {
							addUser = false;
							break;
						}
					}
				}
				if(addUser) {
					customers.add(customerId);
				}
			}
		}
		return customers;
	}
	
	/**
	 * generates a list of Customers who don't have any CustomerAccessControls
	 * @param session
	 * @return List
	 */
	private List<Customer> getCustomersWithoutAccessControl(Session session) {
		List<Customer> allCustomers = session.getDb().customerList();
		List<Customer> customers = new ArrayList<Customer>();
		for(Customer customer: allCustomers) {
			if(!customerAccessTypeHashMap.containsKey(customer.getId())) {
				customers.add(customer);
			}
		}
		return customers;
	}
	
	/**
	 * returns list of customers from a list of customer ids
	 * @param session
	 * @param uuids
	 * @return List
	 */
	private List<Customer> getCustomersFromUUID(Session session, List<UUID> uuids) {
		List<Customer> customerList = new ArrayList<Customer>();
		for(UUID id: uuids) {
			customerList.add(session.getDb().customerFind(id));
		}
		return customerList;
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
