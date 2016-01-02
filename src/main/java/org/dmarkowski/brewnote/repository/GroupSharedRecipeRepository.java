package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.GroupSharedRecipe;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GroupSharedRecipe entity.
 */
public interface GroupSharedRecipeRepository extends JpaRepository<GroupSharedRecipe,Long> {

}
