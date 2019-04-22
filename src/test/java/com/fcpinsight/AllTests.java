/* AllTests.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    com.fcpinsight.customer.CustomerTest.class,
    com.fcpinsight.security.UserRoleTest.class,
})
public class AllTests {

}
