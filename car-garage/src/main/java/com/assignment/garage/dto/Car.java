package com.assignment.garage.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cars")
@Data
@Builder
@NoArgsConstructor
public class Car {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Column(name = "id")
    private Long carId;

    @NotNull
    private String carMake;
    
    @NotNull
    private String carType;
	
	public Car(Long carId, String carMake, String carType)
    {
    	this.carId = carId;
        this.carMake = carMake;
        this.carType = carType;
    }


	public Long getCarId()
    {
        return carId;
    }

    public void setCarId(Long id)
    {
        this.carId = id;
    }

	public String getCarMake() {
		return carMake;
	}

	public void setCarMake(String carMake) {
		this.carMake = carMake;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}
    
}
