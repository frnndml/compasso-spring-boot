package br.com.compasso.test.controller;

import br.com.compasso.test.payload.NamePayload;
import br.com.compasso.test.entity.Customer;
import br.com.compasso.test.service.CustomerService;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List> get() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Customer> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("name/{name}")
    public ResponseEntity<Customer> getByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(customerService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Customer> save(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.save(customer));
    }

    @PatchMapping("{id}")
    @ApiResponse(code = 304, message = "Not Modified")
    public ResponseEntity<Customer> updateName(@RequestBody NamePayload payload, @PathVariable("id") Long id) {
        Customer customer = customerService.findById(id);

        if (Objects.isNull(customer))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        if (Objects.isNull(payload) || Objects.isNull(payload.getName()))
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(customer);


        customer.setName(payload.getName());
        return ResponseEntity.ok(customerService.update(customer));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        Customer found = customerService.findById(id);

        if (Objects.isNull(found))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        customerService.deleteById(found.getId());
        return ResponseEntity.ok(found.getId());
    }
}
