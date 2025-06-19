package hu.bhr.crm.repository.mongo.document;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "customer_details")
public class CustomerDocument implements Persistable<UUID> {

    @Id
    private UUID id;
    private UUID customerId;
    private String note;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public CustomerDocument() {
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

    public CustomerDocument(UUID id, UUID customerId, String note, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.note = note;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "CustomerDocument{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
