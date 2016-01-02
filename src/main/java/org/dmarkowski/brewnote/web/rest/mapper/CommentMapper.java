package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommentMapper {

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "recipe.name", target = "recipeName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    CommentDTO commentToCommentDTO(Comment comment);

    @Mapping(source = "recipeId", target = "recipe")
    @Mapping(source = "userId", target = "user")
    Comment commentDTOToComment(CommentDTO commentDTO);

    default Recipe recipeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Recipe recipe = new Recipe();
        recipe.setId(id);
        return recipe;
    }

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
