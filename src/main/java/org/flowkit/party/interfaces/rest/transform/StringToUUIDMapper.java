package org.flowkit.party.interfaces.rest.transform;

import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper()
public class StringToUUIDMapper {

    public String asString(UUID data) {
        return data != null ? data.toString() : null;
    }

    public UUID asUUID(String data) {
        return data != null ? UUID.fromString(data) : null;
    }
}
