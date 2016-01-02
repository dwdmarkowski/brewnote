package org.dmarkowski.brewnote.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A GroupSharedRecipe.
 */
@Entity
@Table(name = "group_shared_recipe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GroupSharedRecipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sharing_date")
    private ZonedDateTime sharingDate;

    @ManyToOne
    @JoinColumn(name = "friend_group_id")
    private FriendGroup friendGroup;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

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

    public FriendGroup getFriendGroup() {
        return friendGroup;
    }

    public void setFriendGroup(FriendGroup friendGroup) {
        this.friendGroup = friendGroup;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupSharedRecipe groupSharedRecipe = (GroupSharedRecipe) o;
        return Objects.equals(id, groupSharedRecipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GroupSharedRecipe{" +
            "id=" + id +
            ", sharingDate='" + sharingDate + "'" +
            '}';
    }
}
