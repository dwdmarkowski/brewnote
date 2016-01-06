package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Friendship;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Friendship entity.
 */
public interface FriendshipRepository extends JpaRepository<Friendship,Long> {

    @Query("select friendship " +
           "from Friendship friendship " +
           "where (friendship.firstUser.login = ?#{principal.username} and friendship.status = 'Invitation')" +
           "or ((friendship.firstUser.login = ?#{principal.username} or friendship.secondUser.login = ?#{principal.username}) and friendship.status = 'Accepted')")
    Page<Friendship> findInvitationsByFirstUserIsCurrentUser(Pageable pageable);

    @Query("select friendship " +
           "from Friendship friendship " +
           "where friendship.secondUser.login = ?#{principal.username} and friendship.status = 'Invitation'")
    Page<Friendship> findInvitationsSentToMe(Pageable pageable);

    @Query("select friendship from Friendship friendship where friendship.secondUser.login = ?#{principal.username}")
    List<Friendship> findBySecondUserIsCurrentUser();

}
