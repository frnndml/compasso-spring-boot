package br.com.compasso.test.service;

import br.com.compasso.test.entity.City;
import br.com.compasso.test.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public List<City> findByName(String name) {
        return cityRepository.findByName(name);
    }

    public List<City> findAllByState(String state) {
        return cityRepository.findByState(state);
    }

    public City save(City city) {
        return cityRepository.save(city);
    }
}
