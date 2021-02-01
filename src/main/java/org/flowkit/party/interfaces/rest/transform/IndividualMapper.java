package org.flowkit.party.interfaces.rest.transform;

import org.flowkit.party.domain.model.aggregates.Individual;
import org.flowkit.party.domain.model.commands.CreateIndividualCommand;
import org.flowkit.party.interfaces.rest.openapi.dto.CreateIndividualResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = StringToUUIDMapper.class)
public interface IndividualMapper {

    IndividualMapper INSTANCE = Mappers.getMapper(IndividualMapper.class);

    CreateIndividualCommand toCommandFromResource(CreateIndividualResource catalogCreateDTO);

    @Mapping(source = "individualId.id", target = "id")
    org.flowkit.party.interfaces.rest.openapi.dto.Individual toResourceFromAggregate(Individual individual);
}
