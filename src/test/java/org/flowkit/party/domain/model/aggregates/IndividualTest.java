package org.flowkit.party.domain.model.aggregates;

import org.flowkit.party.domain.model.commands.CreateIndividualCommand;
import org.flowkit.party.domain.model.valueobjects.Version;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class IndividualTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Create Individual - Valid data")
    void createIndividualWithValidData() {

        CreateIndividualCommand command = new CreateIndividualCommand();
        command.setFamilyName("John");
        command.setGivenName("Smith");

        Individual individual = new Individual(command);
        assertNotNull("IndividualId must not be null", individual.getIndividualId().getId());
        assertEquals("Version number must be equal to the initial version of Individual Aggregate", individual.getVersion(), Version.initialVersion());
        assertEquals("Family name must match input value", individual.getFamilyName(), command.getFamilyName());
        assertEquals("Given name must match input value", individual.getGivenName(), command.getGivenName());

        Set<ConstraintViolation<Individual>> constraintViolations = validator.validate(individual);
        assertEquals("There should not be any constraintViolations", constraintViolations.size(), 0);
    }

    @Test
    @DisplayName("Create Individual - Empty family & given name")
    void createIndividualWithEmptyFamilyName() {

        CreateIndividualCommand command = new CreateIndividualCommand();
        command.setFamilyName("");
        command.setGivenName("");

        Individual individual = new Individual(command);

        Set<ConstraintViolation<Individual>> constraintViolations = validator.validate(individual);
        assertEquals("constraintViolations must not be empty", constraintViolations.size(), 2);
        assertEquals("constraintViolations must have error message - must not be empty", "must not be empty", constraintViolations.iterator().next().getMessage());
    }

}