package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.Yeast;
import org.dmarkowski.brewnote.repository.YeastRepository;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.YeastDTO;
import org.dmarkowski.brewnote.web.rest.mapper.YeastMapper;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Yeast.
 */
@RestController
@RequestMapping("/api")
public class YeastResource {

    private final Logger log = LoggerFactory.getLogger(YeastResource.class);

    @Inject
    private YeastRepository yeastRepository;

    @Inject
    private YeastMapper yeastMapper;

    /**
     * POST  /yeasts -> Create a new yeast.
     */
    @RequestMapping(value = "/yeasts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<YeastDTO> createYeast(@RequestBody YeastDTO yeastDTO) throws URISyntaxException {
        log.debug("REST request to save Yeast : {}", yeastDTO);
        if (yeastDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new yeast cannot already have an ID").body(null);
        }
        Yeast yeast = yeastMapper.yeastDTOToYeast(yeastDTO);
        Yeast result = yeastRepository.save(yeast);
        return ResponseEntity.created(new URI("/api/yeasts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("yeast", result.getId().toString()))
            .body(yeastMapper.yeastToYeastDTO(result));
    }

    /**
     * PUT  /yeasts -> Updates an existing yeast.
     */
    @RequestMapping(value = "/yeasts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<YeastDTO> updateYeast(@RequestBody YeastDTO yeastDTO) throws URISyntaxException {
        log.debug("REST request to update Yeast : {}", yeastDTO);
        if (yeastDTO.getId() == null) {
            return createYeast(yeastDTO);
        }
        Yeast yeast = yeastMapper.yeastDTOToYeast(yeastDTO);
        Yeast result = yeastRepository.save(yeast);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("yeast", yeastDTO.getId().toString()))
            .body(yeastMapper.yeastToYeastDTO(result));
    }

    /**
     * GET  /yeasts -> get all the yeasts.
     */
    @RequestMapping(value = "/yeasts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<YeastDTO>> getAllYeasts(Pageable pageable)
        throws URISyntaxException {
        Page<Yeast> page = yeastRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/yeasts");
        return new ResponseEntity<>(page.getContent().stream()
            .map(yeastMapper::yeastToYeastDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/yeastsForRecipe/{recipeId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<YeastDTO>> getAllYeastsForRecipe(@PathVariable Long recipeId)
        throws URISyntaxException {
        List<Yeast> yeasts = yeastRepository.findAllForRecipe(recipeId);
        List<YeastDTO> yeastsDTO = new ArrayList<YeastDTO>();
        yeasts.forEach(yeast -> {
            yeastsDTO.add(yeastMapper.yeastToYeastDTO(yeast));
        });
        return new ResponseEntity<List<YeastDTO>>(yeastsDTO, HttpStatus.OK);
    }

    /**
     * GET  /yeasts/:id -> get the "id" yeast.
     */
    @RequestMapping(value = "/yeasts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<YeastDTO> getYeast(@PathVariable Long id) {
        log.debug("REST request to get Yeast : {}", id);
        return Optional.ofNullable(yeastRepository.findOne(id))
            .map(yeastMapper::yeastToYeastDTO)
            .map(yeastDTO -> new ResponseEntity<>(
                yeastDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /yeasts/:id -> delete the "id" yeast.
     */
    @RequestMapping(value = "/yeasts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteYeast(@PathVariable Long id) {
        log.debug("REST request to delete Yeast : {}", id);
        yeastRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("yeast", id.toString())).build();
    }
}
