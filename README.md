# fcptest3
FCP Insight Test3

Implement customer access control for the provided demo system. 

You are given a system that has the following objects: Users, Roles, Customer and AccessControl. The system has a CustomerService for accessing customers and a SecurityService for managing Users and Roles and Authenticating users to the system. 

Add functionality to the CustomerService to restrict access to customers based on access control. You are to complete this assignment by implementing code in the public methods that are marked with a TODO comment.  Feel free to add other private methods or classes to support your solution. Write unit tests to test your code. 

The system has a mock database layer that provides CRUD method for the listed objects.  For this assignment you should assume that it is inexpensive to read the list of access control records from the database. Your test code should not call the database layer directly. Only the service layer should call the database layer. 
The access rules are: 
-	If there is no access control record for a customer, then all users can access the customer. 
-	If the user is a member of a role that has DENY access to a customer, then the user is always denied access to that customer. Denied access take precedence over ALLOW. 
-	If the users is the member of a role that has ALLOW for a customer and is not in a role that has DENY for that customer, then the user is allowed access to the customer. 
-	If there are an ALLOW access records for a customer, but the user is not in a role that has ALLOW access, then the user is denied access to that customer. 
