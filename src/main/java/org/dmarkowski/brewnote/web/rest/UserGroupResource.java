package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.UserGroup;
import org.dmarkowski.brewnote.repository.UserGroupRepository;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.UserGroupDTO;
import org.dmarkowski.brewnote.web.rest.mapper.UserGroupMapper;
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
 * REST controller for managing UserGroup.
 */
@RestController
@RequestMapping("/api")
public class UserGroupResource {

    private final Logger log = LoggerFactory.getLogger(UserGroupResource.class);

    @Inject
    private UserGroupRepository userGroupRepository;

    @Inject
    private UserGroupMapper userGroupMapper;

    /**
     * POST  /userGroups -> Create a new userGroup.
     */
    @RequestMapping(value = "/userGroups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserGroupDTO> createUserGroup(@RequestBody UserGroupDTO userGroupDTO) throws URISyntaxException {
        log.debug("REST request to save UserGroup : {}", userGroupDTO);
        if (userGroupDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userGroup cannot already have an ID").body(null);
        }
        UserGroup userGroup = userGroupMapper.userGroupDTOToUserGroup(userGroupDTO);
        UserGroup result = userGroupRepository.save(userGroup);
        return ResponseEntity.created(new URI("/api/userGroups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userGroup", result.getId().toString()))
            .body(userGroupMapper.userGroupToUserGroupDTO(result));
    }

    /**
     * PUT  /userGroups -> Updates an existing userGroup.
     */
    @RequestMapping(value = "/userGroups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserGroupDTO> updateUserGroup(@RequestBody UserGroupDTO userGroupDTO) throws URISyntaxException {
        log.debug("REST request to update UserGroup : {}", userGroupDTO);
        if (userGroupDTO.getId() == null) {
            return createUserGroup(userGroupDTO);
        }
        UserGroup userGroup = userGroupMapper.userGroupDTOToUserGroup(userGroupDTO);
        UserGroup result = userGroupRepository.save(userGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userGroup", userGroupDTO.getId().toString()))
            .body(userGroupMapper.userGroupToUserGroupDTO(result));
    }

    /**
     * GET  /userGroups -> get all the userGroups.
     */
    @RequestMapping(value = "/userGroups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserGroupDTO>> getAllUserGroups(Pageable pageable)
        throws URISyntaxException {
        Page<UserGroup> page = userGroupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userGroups");
        return new ResponseEntity<>(page.getContent().stream()
            .map(userGroupMapper::userGroupToUserGroupDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /userGroups/:id -> get the "id" userGroup.
     */
    @RequestMapping(value = "/userGroups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserGroupDTO> getUserGroup(@PathVariable Long id) {
        log.debug("REST request to get UserGroup : {}", id);
        return Optional.ofNullable(userGroupRepository.findOne(id))
            .map(userGroupMapper::userGroupToUserGroupDTO)
            .map(userGroupDTO -> new ResponseEntity<>(
                userGroupDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userGroups/:id -> delete the "id" userGroup.
     */
    @RequestMapping(value = "/userGroups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserGroup(@PathVariable Long id) {
        log.debug("REST request to delete UserGroup : {}", id);
        userGroupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userGroup", id.toString())).build();
    }
}
