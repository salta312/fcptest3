/* SystemException.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */

package com.fcpinsight.system;

public class SystemException extends Exception {
	private static final long serialVersionUID = 1L;

    public SystemException(String msg) {
        super(msg);
    }
    public SystemException(String msg, Throwable t) {
        super(msg, t);
    }
	
}
