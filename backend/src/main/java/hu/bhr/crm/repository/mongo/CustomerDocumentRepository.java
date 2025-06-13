package hu.bhr.crm.repository.mongo;

import hu.bhr.crm.repository.mongo.document.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerDocumentRepository extends MongoRepository<CustomerDocument, UUID> {
    void deleteAllByCustomerId(UUID customerId);
}
