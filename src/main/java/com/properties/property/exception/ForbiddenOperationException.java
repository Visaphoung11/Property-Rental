package com.properties.property.exception;

import java.io.Serial;

public class ForbiddenOperationException extends RuntimeException {
	 @Serial
     private static final long serialVersionUID = 1L;
     public ForbiddenOperationException(String message) {
    	 super(message);
     }
}
