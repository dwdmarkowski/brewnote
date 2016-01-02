package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.HopDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Hop and its DTO HopDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HopMapper {

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "recipe.name", target = "recipeName")
    HopDTO hopToHopDTO(Hop hop);

    @Mapping(source = "recipeId", target = "recipe")
    Hop hopDTOToHop(HopDTO hopDTO);

    default Recipe recipeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Recipe recipe = new Recipe();
        recipe.setId(id);
        return recipe;
    }
}
