package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.Mashing;
import org.dmarkowski.brewnote.repository.MashingRepository;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.MashingDTO;
import org.dmarkowski.brewnote.web.rest.mapper.MashingMapper;
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
 * REST controller for managing Mashing.
 */
@RestController
@RequestMapping("/api")
public class MashingResource {

    private final Logger log = LoggerFactory.getLogger(MashingResource.class);

    @Inject
    private MashingRepository mashingRepository;

    @Inject
    private MashingMapper mashingMapper;

    /**
     * POST  /mashings -> Create a new mashing.
     */
    @RequestMapping(value = "/mashings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MashingDTO> createMashing(@RequestBody MashingDTO mashingDTO) throws URISyntaxException {
        log.debug("REST request to save Mashing : {}", mashingDTO);
        if (mashingDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new mashing cannot already have an ID").body(null);
        }
        Mashing mashing = mashingMapper.mashingDTOToMashing(mashingDTO);
        Mashing result = mashingRepository.save(mashing);
        return ResponseEntity.created(new URI("/api/mashings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mashing", result.getId().toString()))
            .body(mashingMapper.mashingToMashingDTO(result));
    }

    /**
     * PUT  /mashings -> Updates an existing mashing.
     */
    @RequestMapping(value = "/mashings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MashingDTO> updateMashing(@RequestBody MashingDTO mashingDTO) throws URISyntaxException {
        log.debug("REST request to update Mashing : {}", mashingDTO);
        if (mashingDTO.getId() == null) {
            return createMashing(mashingDTO);
        }
        Mashing mashing = mashingMapper.mashingDTOToMashing(mashingDTO);
        Mashing result = mashingRepository.save(mashing);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mashing", mashingDTO.getId().toString()))
            .body(mashingMapper.mashingToMashingDTO(result));
    }

    /**
     * GET  /mashings -> get all the mashings.
     */
    @RequestMapping(value = "/mashings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MashingDTO>> getAllMashings(Pageable pageable)
        throws URISyntaxException {
        Page<Mashing> page = mashingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mashings");
        return new ResponseEntity<>(page.getContent().stream()
            .map(mashingMapper::mashingToMashingDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /mashings/:id -> get the "id" mashing.
     */
    @RequestMapping(value = "/mashings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MashingDTO> getMashing(@PathVariable Long id) {
        log.debug("REST request to get Mashing : {}", id);
        return Optional.ofNullable(mashingRepository.findOne(id))
            .map(mashingMapper::mashingToMashingDTO)
            .map(mashingDTO -> new ResponseEntity<>(
                mashingDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mashings/:id -> delete the "id" mashing.
     */
    @RequestMapping(value = "/mashings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMashing(@PathVariable Long id) {
        log.debug("REST request to delete Mashing : {}", id);
        mashingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mashing", id.toString())).build();
    }
}
