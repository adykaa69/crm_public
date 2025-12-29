package hu.bhr.crm.model;

import java.time.Instant;
import java.util.UUID;

public record Customer(
        UUID id,
        String firstName,
        String lastName,
        String nickname,
        String email,
        String phoneNumber,
        String relationship,
        Residence residence,
        Instant createdAt,
        Instant updatedAt
) {
    public Customer withResidence(Residence residence) {
        return new Customer(id, firstName, lastName, nickname, email, phoneNumber, relationship, residence, createdAt, updatedAt);
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public static class CustomerBuilder {
        private UUID id;
        private String firstName;
        private String lastName;
        private String nickname;
        private String email;
        private String phoneNumber;
        private String relationship;
        private Residence residence;
        private Instant createdAt;
        private Instant updatedAt;

        public CustomerBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public CustomerBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CustomerBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CustomerBuilder relationship(String relationship) {
            this.relationship = relationship;
            return this;
        }

        public CustomerBuilder residence(Residence residence) {
            this.residence = residence;
            return this;
        }

        public CustomerBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CustomerBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Customer build() {
            return new Customer(id, firstName, lastName, nickname, email, phoneNumber, relationship, residence, createdAt, updatedAt);
        }
    }
}
