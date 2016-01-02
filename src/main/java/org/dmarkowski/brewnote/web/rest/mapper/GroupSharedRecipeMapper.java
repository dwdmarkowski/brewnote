package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.GroupSharedRecipeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GroupSharedRecipe and its DTO GroupSharedRecipeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GroupSharedRecipeMapper {

    @Mapping(source = "friendGroup.id", target = "friendGroupId")
    @Mapping(source = "friendGroup.name", target = "friendGroupName")
    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "recipe.name", target = "recipeName")
    GroupSharedRecipeDTO groupSharedRecipeToGroupSharedRecipeDTO(GroupSharedRecipe groupSharedRecipe);

    @Mapping(source = "friendGroupId", target = "friendGroup")
    @Mapping(source = "recipeId", target = "recipe")
    GroupSharedRecipe groupSharedRecipeDTOToGroupSharedRecipe(GroupSharedRecipeDTO groupSharedRecipeDTO);

    default FriendGroup friendGroupFromId(Long id) {
        if (id == null) {
            return null;
        }
        FriendGroup friendGroup = new FriendGroup();
        friendGroup.setId(id);
        return friendGroup;
    }

    default Recipe recipeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Recipe recipe = new Recipe();
        recipe.setId(id);
        return recipe;
    }
}
