package org.flowkit.party.domain.model.commands;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NotNull
public class CreateIndividualCommand {

    private @NotEmpty String familyName;
    private @NotEmpty String givenName;
}
