package hu.bhr.crm.repository.mongo;

import hu.bhr.crm.repository.mongo.document.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerDocumentRepository extends MongoRepository<CustomerDocument, String> {

    /**
     * Finds a CustomerDocument by its customerId.
     *
     * @param customerId the unique ID of the customer
     * @return the CustomerDocument if found, otherwise null
     */
    CustomerDocument findByCustomerId(String customerId);
}
