package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.UserGroup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserGroup entity.
 */
public interface UserGroupRepository extends JpaRepository<UserGroup,Long> {

    @Query("select userGroup from UserGroup userGroup where userGroup.user.login = ?#{principal.username}")
    List<UserGroup> findByUserIsCurrentUser();

}
