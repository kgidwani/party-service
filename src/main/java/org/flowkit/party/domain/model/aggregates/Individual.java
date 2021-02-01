package org.flowkit.party.domain.model.aggregates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.flowkit.party.domain.model.commands.CreateIndividualCommand;
import org.flowkit.party.domain.model.valueobjects.Version;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Getter
@AggregateRoot
public class Individual extends AbstractAggregateRoot<Individual> {

    @Id
    private IndividualId individualId;
    private Version version;

    private @NotEmpty String familyName, givenName;
    private String state;

    public Individual(@Valid CreateIndividualCommand createIndividualCommand) {
        this.individualId = IndividualId.create();
        this.version = Version.initialVersion();

        this.familyName = createIndividualCommand.getFamilyName();
        this.givenName = createIndividualCommand.getGivenName();
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    public static class IndividualId implements Identifier {
        UUID id;

        public static IndividualId create() {
            return IndividualId.of(UUID.randomUUID());
            // TODO Create UUID Generator
        }
    }
}
