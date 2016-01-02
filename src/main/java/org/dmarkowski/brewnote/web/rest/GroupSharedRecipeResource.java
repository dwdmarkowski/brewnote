package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.GroupSharedRecipe;
import org.dmarkowski.brewnote.repository.GroupSharedRecipeRepository;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.GroupSharedRecipeDTO;
import org.dmarkowski.brewnote.web.rest.mapper.GroupSharedRecipeMapper;
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
 * REST controller for managing GroupSharedRecipe.
 */
@RestController
@RequestMapping("/api")
public class GroupSharedRecipeResource {

    private final Logger log = LoggerFactory.getLogger(GroupSharedRecipeResource.class);

    @Inject
    private GroupSharedRecipeRepository groupSharedRecipeRepository;

    @Inject
    private GroupSharedRecipeMapper groupSharedRecipeMapper;

    /**
     * POST  /groupSharedRecipes -> Create a new groupSharedRecipe.
     */
    @RequestMapping(value = "/groupSharedRecipes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupSharedRecipeDTO> createGroupSharedRecipe(@RequestBody GroupSharedRecipeDTO groupSharedRecipeDTO) throws URISyntaxException {
        log.debug("REST request to save GroupSharedRecipe : {}", groupSharedRecipeDTO);
        if (groupSharedRecipeDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new groupSharedRecipe cannot already have an ID").body(null);
        }
        GroupSharedRecipe groupSharedRecipe = groupSharedRecipeMapper.groupSharedRecipeDTOToGroupSharedRecipe(groupSharedRecipeDTO);
        GroupSharedRecipe result = groupSharedRecipeRepository.save(groupSharedRecipe);
        return ResponseEntity.created(new URI("/api/groupSharedRecipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("groupSharedRecipe", result.getId().toString()))
            .body(groupSharedRecipeMapper.groupSharedRecipeToGroupSharedRecipeDTO(result));
    }

    /**
     * PUT  /groupSharedRecipes -> Updates an existing groupSharedRecipe.
     */
    @RequestMapping(value = "/groupSharedRecipes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupSharedRecipeDTO> updateGroupSharedRecipe(@RequestBody GroupSharedRecipeDTO groupSharedRecipeDTO) throws URISyntaxException {
        log.debug("REST request to update GroupSharedRecipe : {}", groupSharedRecipeDTO);
        if (groupSharedRecipeDTO.getId() == null) {
            return createGroupSharedRecipe(groupSharedRecipeDTO);
        }
        GroupSharedRecipe groupSharedRecipe = groupSharedRecipeMapper.groupSharedRecipeDTOToGroupSharedRecipe(groupSharedRecipeDTO);
        GroupSharedRecipe result = groupSharedRecipeRepository.save(groupSharedRecipe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("groupSharedRecipe", groupSharedRecipeDTO.getId().toString()))
            .body(groupSharedRecipeMapper.groupSharedRecipeToGroupSharedRecipeDTO(result));
    }

    /**
     * GET  /groupSharedRecipes -> get all the groupSharedRecipes.
     */
    @RequestMapping(value = "/groupSharedRecipes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<GroupSharedRecipeDTO>> getAllGroupSharedRecipes(Pageable pageable)
        throws URISyntaxException {
        Page<GroupSharedRecipe> page = groupSharedRecipeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groupSharedRecipes");
        return new ResponseEntity<>(page.getContent().stream()
            .map(groupSharedRecipeMapper::groupSharedRecipeToGroupSharedRecipeDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /groupSharedRecipes/:id -> get the "id" groupSharedRecipe.
     */
    @RequestMapping(value = "/groupSharedRecipes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupSharedRecipeDTO> getGroupSharedRecipe(@PathVariable Long id) {
        log.debug("REST request to get GroupSharedRecipe : {}", id);
        return Optional.ofNullable(groupSharedRecipeRepository.findOne(id))
            .map(groupSharedRecipeMapper::groupSharedRecipeToGroupSharedRecipeDTO)
            .map(groupSharedRecipeDTO -> new ResponseEntity<>(
                groupSharedRecipeDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /groupSharedRecipes/:id -> delete the "id" groupSharedRecipe.
     */
    @RequestMapping(value = "/groupSharedRecipes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGroupSharedRecipe(@PathVariable Long id) {
        log.debug("REST request to delete GroupSharedRecipe : {}", id);
        groupSharedRecipeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("groupSharedRecipe", id.toString())).build();
    }
}
