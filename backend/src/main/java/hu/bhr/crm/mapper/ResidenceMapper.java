package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.ResidenceRequest;
import hu.bhr.crm.controller.dto.ResidenceResponse;
import hu.bhr.crm.model.Residence;
import hu.bhr.crm.repository.entity.ResidenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResidenceMapper {


    Residence residenceEntityToResidence(ResidenceEntity residenceEntity);

    @Mapping(target = "customer", ignore = true)
    ResidenceEntity residenceToResidenceEntity(Residence residence);

    ResidenceResponse residenceToResidenceResponse(Residence residence);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Residence residenceRequestToResidence(ResidenceRequest residenceRequest);
}
