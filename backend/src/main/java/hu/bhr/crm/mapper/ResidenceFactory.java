package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.ResidenceRequest;
import hu.bhr.crm.model.Residence;

import java.util.UUID;

public class ResidenceFactory {

    /**
     * Builds a Residence from a ResidenceRequest.
     *
     * @param residenceRequest the data transfer object containing the new residence details
     * @return one built residence in a {@link Residence}
     */
    public static Residence createResidence(ResidenceRequest residenceRequest) {
        return Residence.builder()
                .id(UUID.randomUUID())
                .zipCode(residenceRequest.zipCode())
                .streetAddress(residenceRequest.streetAddress())
                .addressLine2(residenceRequest.addressLine2())
                .city(residenceRequest.city())
                .country(residenceRequest.country())
                .build();
    }
}
