package org.flowkit.party.infrastructure.repositories.mongodb;

import org.flowkit.party.domain.model.aggregates.Individual;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface Individuals extends MongoRepository<Individual, UUID> {
}