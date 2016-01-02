package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.YeastDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Yeast and its DTO YeastDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface YeastMapper {

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "recipe.name", target = "recipeName")
    YeastDTO yeastToYeastDTO(Yeast yeast);

    @Mapping(source = "recipeId", target = "recipe")
    Yeast yeastDTOToYeast(YeastDTO yeastDTO);

    default Recipe recipeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Recipe recipe = new Recipe();
        recipe.setId(id);
        return recipe;
    }
}
