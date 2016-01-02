package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.FriendGroup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FriendGroup entity.
 */
public interface FriendGroupRepository extends JpaRepository<FriendGroup,Long> {

}
