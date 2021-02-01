package org.flowkit.party.domain.model.commands;


import lombok.Getter;
import org.flowkit.party.domain.model.aggregates.Individual;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Getter
public class CreateIndividualCommandResult {

    private final Result result;
    private final Individual createdIndividual;
    private final List<String> failureReasons;

    public CreateIndividualCommandResult(@NotNull Individual createdIndividual) {
        this.result = Result.Success;
        this.createdIndividual = createdIndividual;
        this.failureReasons = Collections.emptyList();
    }
}