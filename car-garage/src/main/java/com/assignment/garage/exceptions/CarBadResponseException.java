package com.assignment.garage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CarBadResponseException extends RuntimeException{
	public CarBadResponseException(String message)
    {
        super(message);
    }
}
