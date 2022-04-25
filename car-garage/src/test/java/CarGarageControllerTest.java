
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
//import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.assignment.garage.controllers.CarGarageController;
import com.assignment.garage.dto.Car;
import com.assignment.garage.exceptions.CarBadResponseException;
import com.assignment.garage.exceptions.CarNotFoundException;
import com.assignment.garage.repositories.CarRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CarGarageController.class)
public class CarGarageControllerTest {
	
	@Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    
    @MockBean
    CarRepo carRepo;
    
    
    Car car1 = new Car(1l,"Ford","Fuel");
    Car car2 = new Car(1l,"Honda","Hybrid");
    Car car3 = new Car(1l,"Tesla","Electric");
    
    
    
    @Test
    public void getAllCars_success() throws Exception {
        List<Car> cars = new ArrayList<>(Arrays.asList(car1, car2, car3));
        
        Mockito.when(carRepo.findAll()).thenReturn(cars);
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/car")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].carMake", is("Tesla")));
    }
    
    
    @Test
    public void getCarById_success() throws Exception {
        Mockito.when(carRepo.findById(car1.getCarId())).thenReturn(java.util.Optional.of(car1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/car/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.carType", is("Ford")));
    }
    
    @Test
    public void createCar_success() throws Exception {
        Car car = Car.builder()
                .carMake("Toyota")
                .carType("Fuel")
                .build();

        Mockito.when(carRepo.save(car)).thenReturn(car);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(car));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.carMake", is("Toyota")));
        }
    
    
    @Test
    public void updateCar_success() throws Exception {
        Car updatedCar = Car.builder()
                .carId(1l)
                .carMake("Ford")
                .carType("Fuel")
                .build();

        Mockito.when(carRepo.findById(car1.getCarId())).thenReturn(Optional.of(car1));
        Mockito.when(carRepo.save(updatedCar)).thenReturn(updatedCar);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedCar));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.carMake", is("Ford")));
    }
    
    
    
    @Test
    public void updateCar_nullId() throws Exception {
        Car updatedCar = Car.builder()
                .carMake("Nissan")
                .carType("Fuel")
                .build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedCar));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertTrue(result.getResolvedException() instanceof CarBadResponseException))
        .andExpect(result ->
            assertEquals("Car or ID must not be null!", result.getResolvedException().getMessage()));
        }

    @Test
    public void updateCar_recordNotFound() throws Exception {
        Car updatedCar = Car.builder()
                .carId(5l)
                .carMake("Renault")
                .carType("Fuel")
                .build();

        Mockito.when(carRepo.findById(updatedCar.getCarId())).thenReturn(null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedCar));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertTrue(result.getResolvedException() instanceof CarNotFoundException))
        .andExpect(result ->
            assertEquals("Car with ID 5 does not exist.", result.getResolvedException().getMessage()));
    }
    
    
    
    @Test
    public void deleteCarById_success() throws Exception {
        Mockito.when(carRepo.findById(car2.getCarId())).thenReturn(Optional.of(car2));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/car/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCarById_notFound() throws Exception {
        Mockito.when(carRepo.findById(5l)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/car/2")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof CarNotFoundException))
        .andExpect(result ->
                assertEquals("Car with ID 5 does not exist.", result.getResolvedException().getMessage()));
    }
    
}
