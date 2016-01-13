package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Friendship;

import org.dmarkowski.brewnote.web.rest.dto.FriendshipDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

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

    @Query("select friendship " +
           "from Friendship friendship " +
           "where (friendship.firstUser.id =:firstUserId and friendship.secondUser.id =:secondUserId and (friendship.status = 'Invitation' or friendship.status = 'Accepted')) " +
           "or (friendship.firstUser.id =:secondUserId and friendship.secondUser.id =:firstUserId and (friendship.status = 'Invitation' or friendship.status = 'Accepted'))")
    Friendship findExistingInvitationWithStatusAcceptedOrWaiting(@Param("firstUserId") Long firstUserId, @Param("secondUserId") Long secondUserId);

    @Query("select friendship " +
        "from Friendship friendship " +
        "where (friendship.firstUser.id =:firstUserId and friendship.secondUser.id =:secondUserId and friendship.status = 'Accepted') " +
        "or (friendship.firstUser.id =:secondUserId and friendship.secondUser.id =:firstUserId and friendship.status = 'Accepted')")
    Friendship findAcceptedFriendship(@Param("firstUserId") Long firstUserId, @Param("secondUserId") Long secondUserId);

    @Query("select friendship from Friendship friendship where friendship.secondUser.login = ?#{principal.username} or friendship.firstUser.login = ?#{principal.username} and friendship.status = 'Accepted'")
    List<Friendship> findAcceptedFriendshipsOnly();

    @Query("select count(friendship) " +
        "from Friendship friendship " +
        "where friendship.secondUser.login = ?#{principal.username} and friendship.status = 'Invitation'")
    Integer getNotificationsAmount();
}
