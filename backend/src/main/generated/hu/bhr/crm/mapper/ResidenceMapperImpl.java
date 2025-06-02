package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.ResidenceRequest;
import hu.bhr.crm.controller.dto.ResidenceResponse;
import hu.bhr.crm.model.Residence;
import hu.bhr.crm.repository.entity.ResidenceEntity;
import java.sql.Timestamp;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-02T19:19:00+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class ResidenceMapperImpl implements ResidenceMapper {

    @Override
    public Residence residenceEntityToResidence(ResidenceEntity residenceEntity) {
        if ( residenceEntity == null ) {
            return null;
        }

        Residence.ResidenceBuilder residence = Residence.builder();

        residence.id( residenceEntity.getId() );
        residence.zipCode( residenceEntity.getZipCode() );
        residence.streetAddress( residenceEntity.getStreetAddress() );
        residence.addressLine2( residenceEntity.getAddressLine2() );
        residence.city( residenceEntity.getCity() );
        residence.country( residenceEntity.getCountry() );
        residence.createdAt( residenceEntity.getCreatedAt() );
        residence.updatedAt( residenceEntity.getUpdatedAt() );

        return residence.build();
    }

    @Override
    public ResidenceEntity residenceToResidenceEntity(Residence residence) {
        if ( residence == null ) {
            return null;
        }

        ResidenceEntity residenceEntity = new ResidenceEntity();

        residenceEntity.setId( residence.id() );
        residenceEntity.setZipCode( residence.zipCode() );
        residenceEntity.setStreetAddress( residence.streetAddress() );
        residenceEntity.setAddressLine2( residence.addressLine2() );
        residenceEntity.setCity( residence.city() );
        residenceEntity.setCountry( residence.country() );
        residenceEntity.setCreatedAt( residence.createdAt() );
        residenceEntity.setUpdatedAt( residence.updatedAt() );

        return residenceEntity;
    }

    @Override
    public ResidenceResponse residenceToResidenceResponse(Residence residence) {
        if ( residence == null ) {
            return null;
        }

        UUID id = null;
        String zipCode = null;
        String streetAddress = null;
        String addressLine2 = null;
        String city = null;
        String country = null;
        Timestamp createdAt = null;
        Timestamp updatedAt = null;

        id = residence.id();
        zipCode = residence.zipCode();
        streetAddress = residence.streetAddress();
        addressLine2 = residence.addressLine2();
        city = residence.city();
        country = residence.country();
        createdAt = residence.createdAt();
        updatedAt = residence.updatedAt();

        ResidenceResponse residenceResponse = new ResidenceResponse( id, zipCode, streetAddress, addressLine2, city, country, createdAt, updatedAt );

        return residenceResponse;
    }

    @Override
    public Residence residenceRequestToResidence(ResidenceRequest residenceRequest) {
        if ( residenceRequest == null ) {
            return null;
        }

        Residence.ResidenceBuilder residence = Residence.builder();

        residence.zipCode( residenceRequest.zipCode() );
        residence.streetAddress( residenceRequest.streetAddress() );
        residence.addressLine2( residenceRequest.addressLine2() );
        residence.city( residenceRequest.city() );
        residence.country( residenceRequest.country() );

        return residence.build();
    }
}
