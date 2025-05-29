package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.ResidenceRequest;
import hu.bhr.crm.controller.dto.ResidenceResponse;
import hu.bhr.crm.model.Residence;
import hu.bhr.crm.repository.entity.ResidenceEntity;
import org.springframework.stereotype.Component;

@Component
public class ResidenceMapper {

    public Residence residenceEntityToResidence(ResidenceEntity residenceEntity) {
        if (residenceEntity == null) {
            return null;
        }

        return new Residence(
                residenceEntity.getId(),
                residenceEntity.getZipCode(),
                residenceEntity.getStreetAddress(),
                residenceEntity.getAddressLine2(),
                residenceEntity.getCity(),
                residenceEntity.getCountry(),
                residenceEntity.getCreatedAt(),
                residenceEntity.getUpdatedAt()
        );
    }

    public ResidenceEntity residenceToResidenceEntity(Residence residence) {
        if (residence == null) {
            return null;
        }

        ResidenceEntity residenceEntity = new ResidenceEntity();

        residenceEntity.setId(residence.id());
        residenceEntity.setZipCode(residence.zipCode());
        residenceEntity.setStreetAddress(residence.streetAddress());
        residenceEntity.setAddressLine2(residence.addressLine2());
        residenceEntity.setCity(residence.city());
        residenceEntity.setCountry(residence.country());

        return residenceEntity;
    }

    public ResidenceResponse residenceToResidenceResponse(Residence residence) {
        if (residence == null) {
            return null;
        }

        return new ResidenceResponse(
                residence.id(),
                residence.zipCode(),
                residence.streetAddress(),
                residence.addressLine2(),
                residence.city(),
                residence.country(),
                residence.createdAt(),
                residence.updatedAt()
        );
    }

    public Residence residenceRequestToResidence(ResidenceRequest residenceRequest) {
        if (residenceRequest == null) {
            return null;
        }

        return Residence.builder()
                .zipCode(residenceRequest.zipCode())
                .streetAddress(residenceRequest.streetAddress())
                .addressLine2(residenceRequest.addressLine2())
                .city(residenceRequest.city())
                .country(residenceRequest.country())
                .build();
    }
}
