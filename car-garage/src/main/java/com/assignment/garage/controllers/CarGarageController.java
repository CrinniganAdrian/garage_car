package com.assignment.garage.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.assignment.garage.dto.Car;
import com.assignment.garage.exceptions.CarNotFoundException;
import com.assignment.garage.repositories.CarRepo;

@RestController
@RequestMapping(value = "/car")
public class CarGarageController {

	@Autowired 
	CarRepo carRepo;
	
	
	
	
	@GetMapping
	public List<Car> getAllCars() {
	    return carRepo.findAll();
	}

	@GetMapping(value = "{carId}")
	public Car getCarById(@PathVariable(value="carId") Long carId) {
	    return carRepo.findById(carId).get();
	}
	
	
	
	
	
	@PostMapping
	public Car createCar(@RequestBody @Valid Car car) {
	    return carRepo.save(car);
	}
	
	
	
	
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	class InvalidRequestException extends RuntimeException {
	    public InvalidRequestException(String s) {
	        super(s);
	    }
	}
	
	
	
	
	
	@PutMapping
	public Car updateCar(@RequestBody Car car) throws CarNotFoundException {
	    if (car == null || car.getCarId() == null) {
	        throw new InvalidRequestException("Car or ID must not be null!");
	    }
	    Optional<Car> optionalCar = carRepo.findById(car.getCarId());
	    if (!optionalCar.isPresent()) {
	        throw new CarNotFoundException("Car with ID " + car.getCarId() + " does not exist.");
	    }
	    Car existingCar = optionalCar.get();

	    existingCar.setCarMake(car.getCarMake());
	    existingCar.setCarType(car.getCarType());
		
	    return carRepo.save(existingCar);
	}
	
	
	
	
	
	@DeleteMapping(value = "{carId}")
	public void deleteCarById(@PathVariable(value = "carId") Long carId) throws CarNotFoundException {
	    if (!carRepo.findById(carId).isPresent()) {
	        throw new CarNotFoundException("Car with ID " + carId + " does not exist.");
	    }
	    carRepo.deleteById(carId);
	}
}
