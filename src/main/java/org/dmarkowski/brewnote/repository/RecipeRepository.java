package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Recipe;

import org.dmarkowski.brewnote.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Recipe entity.
 */
public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    @Query("select recipe from Recipe recipe where recipe.user.login = ?#{principal.username}")
    Page<Recipe> findByUserIsCurrentUser(Pageable pageable);

    @Query("select recipe from Recipe recipe where recipe.visibility = 'public'")
    Page<Recipe> findAllPublicRecipes(Pageable pageable);

    @Query("select recipe from Recipe recipe where recipe.visibility = 'friends' and recipe.user.login in :friends")
    Page<Recipe> findAllFriendsRecipes(Pageable pageable, @Param("friends") List<String> friends);

}
