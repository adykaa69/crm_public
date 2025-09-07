package hu.bhr.crm.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public record CustomerDetails(
        UUID id,
        UUID customerId,
        String note,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {

    public static CustomerDetailsBuilder builder() {
    return new CustomerDetailsBuilder();
}

    public static class CustomerDetailsBuilder {
        private UUID id;
        private UUID customerId;
        private String note;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        public CustomerDetailsBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CustomerDetailsBuilder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public CustomerDetailsBuilder note(String note) {
            this.note = note;
            return this;
        }

        public CustomerDetailsBuilder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CustomerDetailsBuilder updatedAt(ZonedDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public CustomerDetails build() {
            return new CustomerDetails(id, customerId, note, createdAt, updatedAt);
        }
    }
}
