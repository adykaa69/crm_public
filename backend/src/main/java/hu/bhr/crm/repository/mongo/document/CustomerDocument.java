package hu.bhr.crm.repository.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CustomerDocument {

    @Id
    String id;
    String customerId;
    String notes;

    public CustomerDocument() {
    }

    public CustomerDocument(String id, String customerId, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "CustomerDocument{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
