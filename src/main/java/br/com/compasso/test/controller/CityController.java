package br.com.compasso.test.controller;

import br.com.compasso.test.entity.City;
import br.com.compasso.test.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<List<City>> get() {
        return ResponseEntity.ok(cityService.findAll());
    }

    @GetMapping("name/{name}")
    public ResponseEntity<City> getByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(cityService.findByName(name));
    }

    @GetMapping("state/{state}")
    public ResponseEntity<List<City>> getByState(@PathVariable("state") String state) {
        return ResponseEntity.ok(cityService.findAllByState(state));
    }

    @PostMapping
    public ResponseEntity<City> save(@RequestBody City city) {
        return ResponseEntity.ok(cityService.save(city));
    }
}
