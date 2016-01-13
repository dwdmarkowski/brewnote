package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.Friendship;
import org.dmarkowski.brewnote.enums.FriendshipStatusE;
import org.dmarkowski.brewnote.repository.FriendshipRepository;
import org.dmarkowski.brewnote.repository.UserRepository;
import org.dmarkowski.brewnote.security.SecurityUtils;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.FriendshipDTO;
import org.dmarkowski.brewnote.web.rest.mapper.FriendshipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Friendship.
 */
@RestController
@RequestMapping("/api")
public class FriendshipResource {

    private final Logger log = LoggerFactory.getLogger(FriendshipResource.class);

    @Inject
    private FriendshipRepository friendshipRepository;

    @Inject
    private FriendshipMapper friendshipMapper;

    @Inject
    private UserRepository userRepository;
    /**
     * POST  /friendships -> Create a new friendship.
     */
    @RequestMapping(value = "/friendships",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FriendshipDTO> createFriendship(@RequestBody FriendshipDTO friendshipDTO) throws URISyntaxException {
        log.debug("REST request to save Friendship : {}", friendshipDTO);
        if (friendshipDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new friendship cannot already have an ID").body(null);
        }
        friendshipDTO.setFirstUserId(userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).get().getId());
        friendshipDTO.setStatus(FriendshipStatusE.INVITATION);
        Friendship friendship = friendshipMapper.friendshipDTOToFriendship(friendshipDTO);
        Friendship result;
        if(friendshipRepository.findExistingInvitationWithStatusAcceptedOrWaiting(friendshipDTO.getFirstUserId(), friendshipDTO.getSecondUserId()) == null){
            result = friendshipRepository.save(friendship);
        } else {
            result = null;
        }
        return ResponseEntity.created(new URI("/api/friendships/"))
            .headers(HeaderUtil.createEntityCreationAlert("friendship", result != null ? result.getId().toString() : null))
            .body(result != null ? friendshipMapper.friendshipToFriendshipDTO(result) : null);
    }

    /**
     * PUT  /friendships -> Updates an existing friendship.
     */
    @RequestMapping(value = "/friendships/accept/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FriendshipDTO> updateFriendship(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to accept Friendship : {}");
        Friendship friendship = friendshipRepository.findOne(id);
        friendship.setStatus(FriendshipStatusE.ACCEPTED);
        Friendship result = friendshipRepository.save(friendship);
        return ResponseEntity.ok()
            .body(friendshipMapper.friendshipToFriendshipDTO(result));
    }

    /**
     * PUT  /friendships -> Updates an existing friendship.
     */
    @RequestMapping(value = "/friendships/reject/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FriendshipDTO> updateFriendshipReject(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to accept Friendship : {}");
        Friendship friendship = friendshipRepository.findOne(id);
        friendship.setStatus(FriendshipStatusE.REJECTED);
        Friendship result = friendshipRepository.save(friendship);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("friendship", id.toString()))
            .body(friendshipMapper.friendshipToFriendshipDTO(result));
    }

    /**
     * GET  /friendships -> get all the friendships.
     */
    @RequestMapping(value = "/friendships",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
         @Timed
         @Transactional(readOnly = true)
         public ResponseEntity<List<FriendshipDTO>> getAllFriendships(Pageable pageable)
        throws URISyntaxException {
        Page<Friendship> page = friendshipRepository.findInvitationsByFirstUserIsCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/friendships");
        List<FriendshipDTO> friendships = page.getContent().stream()
            .map(friendshipMapper::friendshipToFriendshipDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        friendships.forEach(friendshipDTO -> {
            if(friendshipDTO.getSecondUserLogin().equals(SecurityUtils.getCurrentUser().getUsername())) {
                friendshipDTO.setSecondUserLogin(friendshipDTO.getFirstUserLogin());
                friendshipDTO.setSecondUserId(friendshipDTO.getFirstUserId());
                friendshipDTO.setFirstUserLogin(SecurityUtils.getCurrentUser().getUsername());
                friendshipDTO.setFirstUserId(userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).get().getId());
            }
        });
        return new ResponseEntity<>(friendships, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/friendshipNotifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<FriendshipDTO>> getAllFriendhsipNotifications(Pageable pageable)
        throws URISyntaxException {
        Page<Friendship> page = friendshipRepository.findInvitationsSentToMe(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/friendships");
        List<FriendshipDTO> friendships = page.getContent().stream()
            .map(friendshipMapper::friendshipToFriendshipDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return new ResponseEntity<>(friendships, headers, HttpStatus.OK);
    }

    /**
     * GET  /friendships/:id -> get the "id" friendship.
     */
    @RequestMapping(value = "/friendships/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FriendshipDTO> getFriendship(@PathVariable Long id) {
        log.debug("REST request to get Friendship : {}", id);
        return Optional.ofNullable(friendshipRepository.findOne(id))
            .map(friendshipMapper::friendshipToFriendshipDTO)
            .map(friendshipDTO -> new ResponseEntity<>(
                friendshipDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /friendships/:id -> delete the "id" friendship.
     */
    @RequestMapping(value = "/friendships/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFriendship(@PathVariable Long id) {
        log.debug("REST request to delete Friendship : {}", id);
        friendshipRepository.delete(id);
        return ResponseEntity.ok().build();
    }
}
