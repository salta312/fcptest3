/* UserRoleTest.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */


package com.fcpinsight.security;

import com.fcpinsight.security.SecurityService;
import com.fcpinsight.security.Session;
import com.fcpinsight.system.SystemException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;



public class UserRoleTest {
	private static final String USER1_NAME = "user1";
	private static final String USER2_NAME = "user2";
	private static final String USER3_NAME = "user3";
	
	private static final String USER1_PWD = "pwd1";
	private static final String USER2_PWD = "pwd2";
	private static final String USER3_PWD = "pwd3";
	
	private static final String ROLE1_NAME = "role1";
	private static final String ROLE2_NAME = "role2";
	private static final String ROLE3_NAME = "role3";
	
	@Test
	public void testUserReadWrite() throws SystemException {
		SecurityService securityService = new SecurityService();
		
		Session session = securityService.authenticate("admin", "password");
		assertTrue(session != null);

		
		User user1 = new User();
		user1.setUserName(USER1_NAME);
		user1.setPassword(USER1_PWD);
		securityService.saveUser(session, user1);
		
		User user2 = new User();
		user2.setUserName(USER2_NAME);
		user2.setPassword(USER2_PWD);
		securityService.saveUser(session, user2);
		
		User user3 = new User();
		user3.setUserName(USER3_NAME);
		user3.setPassword(USER3_PWD);
		securityService.saveUser(session, user3);
		
		List<User> userList = securityService.userList(session);
		assertEquals(4, userList.size()); // three new plus the default users
		
		User user = securityService.userFind(session, user1.getId());
		assertTrue(user != null);
		assertEquals(USER1_NAME, user.getUserName());
		assertEquals(USER1_PWD, user.getPassword());
		
		user = securityService.userFind(session, user2.getId());
		assertTrue(user != null);
		assertEquals(USER2_NAME, user.getUserName());
		assertEquals(USER2_PWD, user.getPassword());
	
		user = securityService.userFind(session, user3.getId());
		assertTrue(user != null);
		assertEquals(USER3_NAME, user.getUserName());
		assertEquals(USER3_PWD, user.getPassword());
		
	}

	@Test
	public void testUserRoles() throws SystemException {
		SecurityService securityService = new SecurityService();
		
		Session session = securityService.authenticate("admin", "password");
		assertTrue(session != null);

		Role role1 = createRole(session, ROLE1_NAME);
		Role role2 = createRole(session, ROLE2_NAME);
		Role role3 = createRole(session, ROLE3_NAME);

		
		User user1 = new User();
		user1.setUserName(USER1_NAME);
		user1.setPassword(USER1_PWD);
		user1.addRole(role1);
		securityService.saveUser(session, user1);
		
		User user2 = new User();
		user2.setUserName(USER2_NAME);
		user2.setPassword(USER2_PWD);
		user2.addRole(role2);
		securityService.saveUser(session, user2);
		
		User user3 = new User();
		user3.setUserName(USER3_NAME);
		user3.setPassword(USER3_PWD);
		user3.addRole(role1);
		user3.addRole(role2);
		user3.addRole(role3);
		securityService.saveUser(session, user3);
		
		List<User> userList = securityService.userList(session);
		assertEquals(4, userList.size()); // three new plus the default users

		// test user1 roles
		User user = securityService.userFind(session, user1.getId());
		assertTrue(user != null);
		assertEquals(USER1_NAME, user.getUserName());
		
		List<Role> roleList = user.getRoles();
		assertTrue(roleList != null);
		assertEquals(1, roleList.size()); 
		assertTrue(roleList.contains(role1));
		
		// test hasRole for user1
		assertTrue(user.hasRole(role1.getId()));
		assertTrue(!user.hasRole(role2.getId()));
		assertTrue(!user.hasRole(role3.getId()));
		
		// test user2 roles
		user = securityService.userFind(session, user2.getId());
		assertTrue(user != null);
		assertEquals(USER2_NAME, user.getUserName());
		
		roleList = user.getRoles();
		assertTrue(roleList != null);
		assertEquals(1, roleList.size()); 
		assertTrue(roleList.contains(role2));
		
		// test hasRole for user2
		assertTrue(!user.hasRole(role1.getId()));
		assertTrue(user.hasRole(role2.getId()));
		assertTrue(!user.hasRole(role3.getId()));
		
		
		// test user3 roles
		user = securityService.userFind(session, user3.getId());
		assertTrue(user != null);
		assertEquals(USER3_NAME, user.getUserName());
		
		roleList = user.getRoles();
		assertTrue(roleList != null);
		assertEquals(3, roleList.size()); 
		assertTrue(roleList.contains(role1));
		assertTrue(roleList.contains(role2));
		assertTrue(roleList.contains(role3));
		
		
		// test hasRole for user3
		assertTrue(user.hasRole(role1.getId()));
		assertTrue(user.hasRole(role2.getId()));
		assertTrue(user.hasRole(role3.getId()));
		
	}

	private Role createRole(Session session, String roleName) throws SystemException {
		// SecurityService securityService = new SecurityService();
		
		Role role = new Role();
		role.setName(roleName);
		// securityService.saveRole(session, role);
		
		return role;
	}
	
	

}
