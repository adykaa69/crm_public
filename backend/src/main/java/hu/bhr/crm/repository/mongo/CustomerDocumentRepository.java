package hu.bhr.crm.repository.mongo;

import hu.bhr.crm.repository.mongo.document.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerDocumentRepository extends MongoRepository<CustomerDocument, UUID> {
    // Delete all documents related to a customer by customer ID
    void deleteAllByCustomerId(UUID customerId);

    // Find all documents related to a customer by customer ID
    List<CustomerDocument> findAllByCustomerId(UUID customerId);
}
