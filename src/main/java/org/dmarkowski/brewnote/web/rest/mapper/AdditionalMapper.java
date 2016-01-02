package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.AdditionalDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Additional and its DTO AdditionalDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AdditionalMapper {

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "recipe.name", target = "recipeName")
    AdditionalDTO additionalToAdditionalDTO(Additional additional);

    @Mapping(source = "recipeId", target = "recipe")
    Additional additionalDTOToAdditional(AdditionalDTO additionalDTO);

    default Recipe recipeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Recipe recipe = new Recipe();
        recipe.setId(id);
        return recipe;
    }
}
