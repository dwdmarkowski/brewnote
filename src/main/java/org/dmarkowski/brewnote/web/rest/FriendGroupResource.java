package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.FriendGroup;
import org.dmarkowski.brewnote.repository.FriendGroupRepository;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.FriendGroupDTO;
import org.dmarkowski.brewnote.web.rest.mapper.FriendGroupMapper;
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
 * REST controller for managing FriendGroup.
 */
@RestController
@RequestMapping("/api")
public class FriendGroupResource {

    private final Logger log = LoggerFactory.getLogger(FriendGroupResource.class);

    @Inject
    private FriendGroupRepository friendGroupRepository;

    @Inject
    private FriendGroupMapper friendGroupMapper;

    /**
     * POST  /friendGroups -> Create a new friendGroup.
     */
    @RequestMapping(value = "/friendGroups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FriendGroupDTO> createFriendGroup(@RequestBody FriendGroupDTO friendGroupDTO) throws URISyntaxException {
        log.debug("REST request to save FriendGroup : {}", friendGroupDTO);
        if (friendGroupDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new friendGroup cannot already have an ID").body(null);
        }
        FriendGroup friendGroup = friendGroupMapper.friendGroupDTOToFriendGroup(friendGroupDTO);
        FriendGroup result = friendGroupRepository.save(friendGroup);
        return ResponseEntity.created(new URI("/api/friendGroups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("friendGroup", result.getId().toString()))
            .body(friendGroupMapper.friendGroupToFriendGroupDTO(result));
    }

    /**
     * PUT  /friendGroups -> Updates an existing friendGroup.
     */
    @RequestMapping(value = "/friendGroups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FriendGroupDTO> updateFriendGroup(@RequestBody FriendGroupDTO friendGroupDTO) throws URISyntaxException {
        log.debug("REST request to update FriendGroup : {}", friendGroupDTO);
        if (friendGroupDTO.getId() == null) {
            return createFriendGroup(friendGroupDTO);
        }
        FriendGroup friendGroup = friendGroupMapper.friendGroupDTOToFriendGroup(friendGroupDTO);
        FriendGroup result = friendGroupRepository.save(friendGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("friendGroup", friendGroupDTO.getId().toString()))
            .body(friendGroupMapper.friendGroupToFriendGroupDTO(result));
    }

    /**
     * GET  /friendGroups -> get all the friendGroups.
     */
    @RequestMapping(value = "/friendGroups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<FriendGroupDTO>> getAllFriendGroups(Pageable pageable)
        throws URISyntaxException {
        Page<FriendGroup> page = friendGroupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/friendGroups");
        return new ResponseEntity<>(page.getContent().stream()
            .map(friendGroupMapper::friendGroupToFriendGroupDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /friendGroups/:id -> get the "id" friendGroup.
     */
    @RequestMapping(value = "/friendGroups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FriendGroupDTO> getFriendGroup(@PathVariable Long id) {
        log.debug("REST request to get FriendGroup : {}", id);
        return Optional.ofNullable(friendGroupRepository.findOne(id))
            .map(friendGroupMapper::friendGroupToFriendGroupDTO)
            .map(friendGroupDTO -> new ResponseEntity<>(
                friendGroupDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /friendGroups/:id -> delete the "id" friendGroup.
     */
    @RequestMapping(value = "/friendGroups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFriendGroup(@PathVariable Long id) {
        log.debug("REST request to delete FriendGroup : {}", id);
        friendGroupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("friendGroup", id.toString())).build();
    }
}
