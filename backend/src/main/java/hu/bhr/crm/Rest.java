package hu.bhr.crm;

import hu.bhr.crm.repository.CustomerRepository;
import hu.bhr.crm.repository.entity.CustomerEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {

    private final CustomerRepository repository;

    public Rest(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/test")
    public String getTest() {
        return repository.findAll().stream()
                .findFirst()
                .map(CustomerEntity::toString)
                .orElse("Database is empty");
    }
}
