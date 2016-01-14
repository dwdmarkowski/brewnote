package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Yeast;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Yeast entity.
 */
public interface YeastRepository extends JpaRepository<Yeast,Long> {

    @Query("Select yeast from Yeast yeast where yeast.recipe.id=:recipeId")
    List<Yeast> findAllForRecipe(@Param("recipeId") Long recipeId);

}
