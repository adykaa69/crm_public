package hu.bhr.crm;

import hu.bhr.crm.repository.mongo.CustomerDocumentRepository;
import hu.bhr.crm.repository.mongo.document.CustomerDocument;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Test configuration class used for testing MongoDB connection
@Configuration
public class MongoConnectionTest {

    @Bean
    CommandLineRunner testMongoConnection(CustomerDocumentRepository repository) {
        return args -> {
            // Create a new CustomerDocument
            CustomerDocument customerDocument = new CustomerDocument("1", "12345", "Test notes");
            // Save the document to MongoDB
            repository.save(customerDocument);
            // Fetch the document by customerId
            CustomerDocument fetchedDocument = repository.findByCustomerId("12345");
            // Print the fetched document
            System.out.println(fetchedDocument);
        };
    }
}
