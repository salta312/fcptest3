/* InvalidSessionException.java
 * 
 * Copyright (c) FCP Insight, Inc. 2019. All rights reserved.
 */
 
package com.fcpinsight.system;

public class InvalidSessionException extends SystemException {
	private static final long serialVersionUID = 1L;

    public InvalidSessionException(String msg) {
        super(msg);
    }
    public InvalidSessionException(String msg, Throwable t) {
        super(msg, t);
    }
}
