package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Friendship;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Friendship entity.
 */
public interface FriendshipRepository extends JpaRepository<Friendship,Long> {

    @Query("select friendship from Friendship friendship where friendship.firstUser.login = ?#{principal.username}")
    List<Friendship> findByFirstUserIsCurrentUser();

    @Query("select friendship from Friendship friendship where friendship.secondUser.login = ?#{principal.username}")
    List<Friendship> findBySecondUserIsCurrentUser();

}
