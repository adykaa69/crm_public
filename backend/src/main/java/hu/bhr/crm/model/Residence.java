package hu.bhr.crm.model;

import java.time.Instant;
import java.util.UUID;

public record Residence(
        UUID id,
        String zipCode,
        String streetAddress,
        String addressLine2,
        String city,
        String country,
        Instant createdAt,
        Instant updatedAt
) {

    public Residence withId(UUID id) {
        return new Residence(id, zipCode, streetAddress, addressLine2, city, country, createdAt, updatedAt);
    }

    public static ResidenceBuilder builder() {
        return new ResidenceBuilder();
    }

    public static class ResidenceBuilder {
        private UUID id;
        private String zipCode;
        private String streetAddress;
        private String addressLine2;
        private String city;
        private String country;
        private Instant createdAt;
        private Instant updatedAt;

        public ResidenceBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ResidenceBuilder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public ResidenceBuilder streetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
            return this;
        }

        public ResidenceBuilder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public ResidenceBuilder city(String city) {
            this.city = city;
            return this;
        }

        public ResidenceBuilder country(String country) {
            this.country = country;
            return this;
        }

        public ResidenceBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ResidenceBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Residence build() {
            return new Residence(id, zipCode, streetAddress, addressLine2, city, country, createdAt, updatedAt);
        }
    }
}
