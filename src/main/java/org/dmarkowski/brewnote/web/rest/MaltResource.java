package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.Malt;
import org.dmarkowski.brewnote.repository.MaltRepository;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.MaltDTO;
import org.dmarkowski.brewnote.web.rest.mapper.MaltMapper;
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
 * REST controller for managing Malt.
 */
@RestController
@RequestMapping("/api")
public class MaltResource {

    private final Logger log = LoggerFactory.getLogger(MaltResource.class);

    @Inject
    private MaltRepository maltRepository;

    @Inject
    private MaltMapper maltMapper;

    /**
     * POST  /malts -> Create a new malt.
     */
    @RequestMapping(value = "/malts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MaltDTO> createMalt(@RequestBody MaltDTO maltDTO) throws URISyntaxException {
        log.debug("REST request to save Malt : {}", maltDTO);
        if (maltDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new malt cannot already have an ID").body(null);
        }
        Malt malt = maltMapper.maltDTOToMalt(maltDTO);
        Malt result = maltRepository.save(malt);
        return ResponseEntity.created(new URI("/api/malts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("malt", result.getId().toString()))
            .body(maltMapper.maltToMaltDTO(result));
    }

    /**
     * PUT  /malts -> Updates an existing malt.
     */
    @RequestMapping(value = "/malts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MaltDTO> updateMalt(@RequestBody MaltDTO maltDTO) throws URISyntaxException {
        log.debug("REST request to update Malt : {}", maltDTO);
        if (maltDTO.getId() == null) {
            return createMalt(maltDTO);
        }
        Malt malt = maltMapper.maltDTOToMalt(maltDTO);
        Malt result = maltRepository.save(malt);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("malt", maltDTO.getId().toString()))
            .body(maltMapper.maltToMaltDTO(result));
    }

    /**
     * GET  /malts -> get all the malts.
     */
    @RequestMapping(value = "/malts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MaltDTO>> getAllMalts(Pageable pageable)
        throws URISyntaxException {
        Page<Malt> page = maltRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/malts");
        return new ResponseEntity<>(page.getContent().stream()
            .map(maltMapper::maltToMaltDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /malts/:id -> get the "id" malt.
     */
    @RequestMapping(value = "/malts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MaltDTO> getMalt(@PathVariable Long id) {
        log.debug("REST request to get Malt : {}", id);
        return Optional.ofNullable(maltRepository.findOne(id))
            .map(maltMapper::maltToMaltDTO)
            .map(maltDTO -> new ResponseEntity<>(
                maltDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /malts/:id -> delete the "id" malt.
     */
    @RequestMapping(value = "/malts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMalt(@PathVariable Long id) {
        log.debug("REST request to delete Malt : {}", id);
        maltRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("malt", id.toString())).build();
    }
}
