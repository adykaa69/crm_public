package hu.bhr.crm.repository.mongo.document;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.UUID;

@Document(collection = "customer_details")
public class CustomerDocument implements Persistable<UUID> {

    @Id
    private UUID id;
    private UUID customerId;
    private String note;

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    public CustomerDocument() {
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

    public CustomerDocument(UUID id, UUID customerId, String note, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
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
