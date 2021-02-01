package org.flowkit.party.application.commandservices;

import org.flowkit.party.domain.model.aggregates.Individual;
import org.flowkit.party.domain.model.commands.CreateIndividualCommand;
import org.flowkit.party.domain.model.commands.CreateIndividualCommandResult;
import org.flowkit.party.infrastructure.repositories.mongodb.Individuals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Transactional
@Validated
public class IndividualCommandService {

    private final Individuals individuals;

    @Autowired
    public IndividualCommandService(Individuals individuals) {
        this.individuals = individuals;
    }

    public CreateIndividualCommandResult createIndividual(@Valid CreateIndividualCommand createIndividualCommand) {
        Individual createdIndividual = individuals.save(new Individual(createIndividualCommand));
        return new CreateIndividualCommandResult(createdIndividual);
    }

}
