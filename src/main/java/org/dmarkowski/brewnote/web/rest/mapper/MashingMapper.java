package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.MashingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Mashing and its DTO MashingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MashingMapper {

    @Mapping(source = "malt.id", target = "maltId")
    @Mapping(source = "malt.name", target = "maltName")
    MashingDTO mashingToMashingDTO(Mashing mashing);

    @Mapping(source = "maltId", target = "malt")
    Mashing mashingDTOToMashing(MashingDTO mashingDTO);

    default Malt maltFromId(Long id) {
        if (id == null) {
            return null;
        }
        Malt malt = new Malt();
        malt.setId(id);
        return malt;
    }
}
