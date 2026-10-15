package com.example.location;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LocationWebService {

    private final List<Car> cars = new ArrayList<>();

    public LocationWebService() {
        cars.add(new Car("11AA22", "Ferrari", 100));
        cars.add(new Car("AA11BB", "Renault", 50));
        cars.add(new Car("22CC33", "BMW", 80));
    }

    @GetMapping("/bonjour")
    public String bonjour() {
        return "bonjour";
    }

    @GetMapping("/cars")
    @ResponseStatus(HttpStatus.OK)
    public List<Car> listOfCars() {
        return cars.stream()
                .filter(car -> !car.isRented())
                .toList();
    }

    @GetMapping("/cars/{plateNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Car aCar(@PathVariable String plateNumber) {
        return findCar(plateNumber);
    }

    @PutMapping("/cars/{plateNumber}")
    @ResponseStatus(HttpStatus.OK)
    public void rentOrGetBack(
            @PathVariable String plateNumber,
            @RequestParam boolean rent,
            @RequestBody(required = false) Dates dates) {

        Car car = findCar(plateNumber);

        car.setRented(rent);

        if (rent) {
            System.out.println(
                    "Voiture " + plateNumber +
                    " louée du " +
                    dates.getBegin() +
                    " au " +
                    dates.getEnd()
            );
        } else {
            System.out.println("Voiture " + plateNumber + " rendue.");
        }
    }

    private Car findCar(String plateNumber) {
        return cars.stream()
                .filter(car -> car.getPlateNumber().equals(plateNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Voiture introuvable : " + plateNumber
                ));
    }
}

//./gradlew bootRun