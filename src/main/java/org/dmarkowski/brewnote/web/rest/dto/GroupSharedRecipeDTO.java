package org.dmarkowski.brewnote.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the GroupSharedRecipe entity.
 */
public class GroupSharedRecipeDTO implements Serializable {

    private Long id;

    private ZonedDateTime sharingDate;

    private Long friendGroupId;

    private String friendGroupName;

    private Long recipeId;

    private String recipeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSharingDate() {
        return sharingDate;
    }

    public void setSharingDate(ZonedDateTime sharingDate) {
        this.sharingDate = sharingDate;
    }

    public Long getFriendGroupId() {
        return friendGroupId;
    }

    public void setFriendGroupId(Long friendGroupId) {
        this.friendGroupId = friendGroupId;
    }

    public String getFriendGroupName() {
        return friendGroupName;
    }

    public void setFriendGroupName(String friendGroupName) {
        this.friendGroupName = friendGroupName;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupSharedRecipeDTO groupSharedRecipeDTO = (GroupSharedRecipeDTO) o;

        if ( ! Objects.equals(id, groupSharedRecipeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GroupSharedRecipeDTO{" +
            "id=" + id +
            ", sharingDate='" + sharingDate + "'" +
            '}';
    }
}
