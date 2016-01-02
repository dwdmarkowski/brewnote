package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.RecipeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Recipe and its DTO RecipeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RecipeMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    RecipeDTO recipeToRecipeDTO(Recipe recipe);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "groupSharedRecipes", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "malts", ignore = true)
    @Mapping(target = "hops", ignore = true)
    @Mapping(target = "yeasts", ignore = true)
    @Mapping(target = "additionals", ignore = true)
    Recipe recipeDTOToRecipe(RecipeDTO recipeDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
