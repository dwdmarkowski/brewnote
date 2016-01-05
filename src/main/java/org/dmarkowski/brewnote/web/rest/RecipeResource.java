package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.Recipe;
import org.dmarkowski.brewnote.repository.RecipeRepository;
import org.dmarkowski.brewnote.repository.UserRepository;
import org.dmarkowski.brewnote.security.SecurityUtils;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.RecipeDTO;
import org.dmarkowski.brewnote.web.rest.mapper.RecipeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * REST controller for managing Recipe.
 */
@RestController
@RequestMapping("/api")
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    @Inject
    private RecipeRepository recipeRepository;

    @Inject
    private RecipeMapper recipeMapper;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /recipes -> Create a new recipe.
     */
    @RequestMapping(value = "/recipes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipeDTO);
        if (recipeDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new recipe cannot already have an ID").body(null);
        }
        recipeDTO.setUserId(userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).get().getId());
        Recipe recipe = recipeMapper.recipeDTOToRecipe(recipeDTO);
        Recipe result = recipeRepository.save(recipe);
        return ResponseEntity.created(new URI("/api/recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("recipe", result.getId().toString()))
            .body(recipeMapper.recipeToRecipeDTO(result));
    }

    /**
     * PUT  /recipes -> Updates an existing recipe.
     */
    @RequestMapping(value = "/recipes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecipeDTO> updateRecipe(@RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to update Recipe : {}", recipeDTO);
        if (recipeDTO.getId() == null) {
            return createRecipe(recipeDTO);
        }
        Recipe recipe = recipeMapper.recipeDTOToRecipe(recipeDTO);
        Recipe result = recipeRepository.save(recipe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("recipe", recipeDTO.getId().toString()))
            .body(recipeMapper.recipeToRecipeDTO(result));
    }

    /**
     * GET  /recipes -> get all the recipes.
     */
    @RequestMapping(value = "/recipes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(Pageable pageable)
        throws URISyntaxException {
        Page<Recipe> page = recipeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/recipes");
        return new ResponseEntity<>(page.getContent().stream()
            .map(recipeMapper::recipeToRecipeDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /recipes/:id -> get the "id" recipe.
     */
    @RequestMapping(value = "/recipes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable Long id) {
        log.debug("REST request to get Recipe : {}", id);
        return Optional.ofNullable(recipeRepository.findOne(id))
            .map(recipeMapper::recipeToRecipeDTO)
            .map(recipeDTO -> new ResponseEntity<>(
                recipeDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /recipes/:id -> delete the "id" recipe.
     */
    @RequestMapping(value = "/recipes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        log.debug("REST request to delete Recipe : {}", id);
        recipeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("recipe", id.toString())).build();
    }
}
