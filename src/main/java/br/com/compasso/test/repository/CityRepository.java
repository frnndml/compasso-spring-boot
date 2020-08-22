package br.com.compasso.test.repository;

import br.com.compasso.test.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    City findByName(String name);

    List<City> findByState(String state);
}