package com.assignment.garage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.garage.dto.Car;

public interface CarRepo extends JpaRepository<Car,Long>{

}
