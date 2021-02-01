package org.flowkit.party.interfaces.rest;

import lombok.extern.slf4j.Slf4j;
import org.flowkit.party.application.commandservices.IndividualCommandService;
import org.flowkit.party.domain.model.commands.CreateIndividualCommand;
import org.flowkit.party.domain.model.commands.CreateIndividualCommandResult;
import org.flowkit.party.domain.model.commands.Result;
import org.flowkit.party.interfaces.rest.openapi.IndividualApiDelegate;
import org.flowkit.party.interfaces.rest.openapi.dto.CreateIndividualResource;
import org.flowkit.party.interfaces.rest.openapi.dto.Individual;
import org.flowkit.party.interfaces.rest.transform.IndividualMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zalando.problem.Problem;

import java.net.URI;

import static org.zalando.problem.Status.CONFLICT;

@Service
@Slf4j
public class IndividualApiDelegateImpl implements IndividualApiDelegate {

    private final IndividualCommandService commandService;

    public IndividualApiDelegateImpl(IndividualCommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public ResponseEntity<Individual> createIndividual(CreateIndividualResource createIndividualResource) {

        log.debug("Creating Individual");
        CreateIndividualCommand command = getCreateIndividualCommand(createIndividualResource);
        CreateIndividualCommandResult commandResult = commandService.createIndividual(command);

        if (commandResult.getResult().equals(Result.Success)) {
            Individual individual = getIndividualResource(commandResult);

            URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().
                    path("/{idOfNewResource}").buildAndExpand(individual.getId()).toUri();
            individual.setHref(location.toString());
            String eTag = commandResult.getCreatedIndividual().getVersion().getVersion().toString();

            return ResponseEntity.created(location).eTag(eTag).body(individual);
        } else {
            throw Problem.builder()
                    .withTitle(CONFLICT.getReasonPhrase())
                    .withStatus(CONFLICT)
                    .withDetail(commandResult.getFailureReasons().get(0))
                    .build();
        }
    }

    private Individual getIndividualResource(CreateIndividualCommandResult commandResult) {
        return IndividualMapper.INSTANCE.toResourceFromAggregate(commandResult.getCreatedIndividual());
    }

    private CreateIndividualCommand getCreateIndividualCommand(CreateIndividualResource createIndividualResource) {
        return IndividualMapper.INSTANCE.toCommandFromResource(createIndividualResource);
    }

}
