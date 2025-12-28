package com.properties.property.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.http.HttpStatus;


import com.properties.property.dto.ApiResponseWithSuccess;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseWithSuccess> handleNotFound(ResourceNotFoundException ex) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(new ApiResponseWithSuccess(false, ex.getMessage(), null));
	}

	
	 @ExceptionHandler(ForbiddenOperationException.class)
	 public ResponseEntity<ApiResponseWithSuccess> handleForbidden(ForbiddenOperationException ex) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(new ApiResponseWithSuccess(false, ex.getMessage(), null));
	    }
	 
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponseWithSuccess> handleForbidden(AccessDeniedException ex){
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ApiResponseWithSuccess(false, ex.getMessage(), null));
	}
	
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseWithSuccess> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponseWithSuccess(false, "Internal server error", null));
    }
	
	
}
