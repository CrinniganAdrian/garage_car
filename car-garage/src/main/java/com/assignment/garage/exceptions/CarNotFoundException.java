package com.assignment.garage.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CarNotFoundException extends RuntimeException{
	public CarNotFoundException(String message)
    {
        super(message);
    }
}
