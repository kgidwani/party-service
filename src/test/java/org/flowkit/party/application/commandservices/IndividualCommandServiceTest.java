package org.flowkit.party.application.commandservices;

import org.flowkit.party.domain.model.aggregates.Individual;
import org.flowkit.party.domain.model.commands.CreateIndividualCommand;
import org.flowkit.party.domain.model.commands.CreateIndividualCommandResult;
import org.flowkit.party.domain.model.commands.Result;
import org.flowkit.party.infrastructure.repositories.mongodb.Individuals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class IndividualCommandServiceTest {

    @Autowired
    private IndividualCommandService commandService;

    @MockBean
    private Individuals individuals;

    @Test
    void createIndividual() {

        CreateIndividualCommand command = new CreateIndividualCommand();
        command.setFamilyName("Lamborgizzia");
        command.setGivenName("Jane");

        Individual individual = new Individual(command);

        doReturn(individual).when(individuals).save(any());

        CreateIndividualCommandResult commandResult = commandService.createIndividual(command);
        Assertions.assertEquals(commandResult.getResult(), Result.Success);
        Assertions.assertEquals(individual.getIndividualId(), commandResult.getCreatedIndividual().getIndividualId());
        Assertions.assertEquals(commandResult.getFailureReasons().size(), 0);
    }
}