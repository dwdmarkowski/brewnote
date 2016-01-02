package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.MaltDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Malt and its DTO MaltDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MaltMapper {

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "recipe.name", target = "recipeName")
    MaltDTO maltToMaltDTO(Malt malt);

    @Mapping(source = "recipeId", target = "recipe")
    @Mapping(target = "mashings", ignore = true)
    Malt maltDTOToMalt(MaltDTO maltDTO);

    default Recipe recipeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Recipe recipe = new Recipe();
        recipe.setId(id);
        return recipe;
    }
}
