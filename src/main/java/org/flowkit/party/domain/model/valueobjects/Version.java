package org.flowkit.party.domain.model.valueobjects;

import lombok.Value;

@Value
public class Version {
    Integer version;

    public static Version initialVersion() {
        return new Version(1);
    }
}
