package br.com.compasso.test;

import br.com.compasso.test.entity.City;
import br.com.compasso.test.entity.Customer;
import br.com.compasso.test.payload.NamePayload;
import br.com.compasso.test.repository.CityRepository;
import br.com.compasso.test.repository.CustomerRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CompassoApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	@Order(1)
	public void testGetCity_thenStatus200() throws Exception {
		createCity(City.builder().name("Florianópolis").state("SC").build());
		createCity(City.builder().name("São José").state("SC").build());

		mvc.perform(get("/cities").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
				.andExpect(jsonPath("$[0].name", is("Florianópolis")))
				.andExpect(jsonPath("$[1].name", is("São José")));
	}

	@Test
	@Order(2)
	public void testGetCityByName_thenStatus200() throws Exception {
		createCity(City.builder().name("Lages").state("SC").build());

		mvc.perform(get("/cities/name/{name}", "Lages").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is("Lages")));

		cityRepository.deleteAll();
	}

	@Test
	@Order(3)
	public void testGetCityByState_thenStatus200() throws Exception {
		createCity(City.builder().name("Florianópolis").state("SC").build());
		createCity(City.builder().name("São José").state("SC").build());
		createCity(City.builder().name("Porto Alegre").state("RS").build());

		mvc.perform(get("/cities/state/{state}", "RS").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(equalTo(1))))
				.andExpect(jsonPath("$[0].name", is("Porto Alegre")));

		cityRepository.deleteAll();
	}

	@Test
	@Order(4)
	public void testSave_theStatus200() throws Exception{
		City city = City.builder().name("Palhoça").state("SC").build();

		mvc.perform(post("/cities").contentType(MediaType.APPLICATION_JSON).content(toJson(city)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is("Palhoça")));

		cityRepository.deleteAll();
	}

	@Test
	@Order(5)
	public void testGetCustomer_thenStatus200() throws Exception {
		City city = createCity(City.builder().name("Florianópolis").state("SC").build());

		Customer customer = createCustomer(Customer.builder().name("Customer").age(30).birthday(LocalDate.of(1990, 1, 1)).gender("M").city(city).build());
		Customer anotherCustomer = createCustomer(Customer.builder().name("Another Customer").age(35).birthday(LocalDate.of(1985, 1, 1)).gender("F").city(city).build());

		mvc.perform(get("/customers").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
				.andExpect(jsonPath("$[0].name", is(customer.getName())))
				.andExpect(jsonPath("$[1].name", is(anotherCustomer.getName())));

		customerRepository.deleteAll();
		cityRepository.deleteAll();
	}

	@Test
	@Order(6)
	public void testGetCustomerById_thenStatus200() throws Exception {
		City city = createCity(City.builder().name("Florianópolis").state("SC").build());

		Customer customer = createCustomer(Customer.builder().name("Customer").age(30).birthday(LocalDate.of(1990, 1, 1)).gender("M").city(city).build());

		mvc.perform(get("/customers/{id}", customer.getId()).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(customer.getName())));

		customerRepository.deleteAll();
		cityRepository.deleteAll();
	}

	@Test
	@Order(7)
	public void testGetCustomerByName_thenStatus200() throws Exception {
		City city = createCity(City.builder().name("Florianópolis").state("SC").build());

		Customer customer = createCustomer(Customer.builder().name("Customer").age(30).birthday(LocalDate.of(1990, 1, 1)).gender("M").city(city).build());

		mvc.perform(get("/customers/name/{name}", customer.getName()).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(customer.getName())));

		customerRepository.deleteAll();
		cityRepository.deleteAll();
	}

	@Test
	@Order(8)
	public void testUpdateName_thenStatus200() throws Exception {
		City city = createCity(City.builder().name("Florianópolis").state("SC").build());

		Customer customer = createCustomer(Customer.builder().name("Customer").age(30).birthday(LocalDate.of(1990, 1, 1)).gender("M").city(city).build());

		mvc.perform(patch("/customers/{id}", customer.getId()).content(toJson(NamePayload.builder().name("New Name").build())).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(customer.getId().intValue())))
				.andExpect(jsonPath("$.name", is("New Name")));

		customerRepository.deleteAll();
		cityRepository.deleteAll();
	}

	@Test
	@Order(9)
	public void testUpdateNameWithNullValue_thenStatus304() throws Exception {
		City city = createCity(City.builder().name("Florianópolis").state("SC").build());

		Customer customer = createCustomer(Customer.builder().name("Customer").age(30).birthday(LocalDate.of(1990, 1, 1)).gender("M").city(city).build());

		mvc.perform(patch("/customers/{id}", customer.getId()).content(toJson(NamePayload.builder().build())).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotModified());

		customerRepository.deleteAll();
		cityRepository.deleteAll();
	}

	@Test
	@Order(10)
	public void testUpdateNameOfNonExistentCustomer_thenStatus404() throws Exception {
		customerRepository.deleteAll();
		cityRepository.deleteAll();

		mvc.perform(patch("/customers/{id}", 1).content(toJson(NamePayload.builder().name("New Name").build())).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());

		customerRepository.deleteAll();
		cityRepository.deleteAll();
	}

	@Test
	@Order(11)
	public void testDeleteCustomer_thenStatus200() throws Exception {
		City city = createCity(City.builder().name("Florianópolis").state("SC").build());

		Customer customer = createCustomer(Customer.builder().name("Customer").age(30).birthday(LocalDate.of(1990, 1, 1)).gender("M").city(city).build());

		mvc.perform(delete("/customers/{id}", customer.getId()).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", is(customer.getId().intValue())));

		customerRepository.deleteAll();
		cityRepository.deleteAll();
	}

	@Test
	@Order(12)
	public void testDeleteNonExistentCustomer_thenStatus404() throws Exception {
		customerRepository.deleteAll();
		cityRepository.deleteAll();

		mvc.perform(delete("/customers/{id}", 1).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	private City createCity(City city) {
		return cityRepository.save(city);
	}

	private Customer createCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	private byte[] toJson(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
}
