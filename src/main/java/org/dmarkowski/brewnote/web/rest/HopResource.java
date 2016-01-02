package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.Hop;
import org.dmarkowski.brewnote.repository.HopRepository;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.HopDTO;
import org.dmarkowski.brewnote.web.rest.mapper.HopMapper;
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
 * REST controller for managing Hop.
 */
@RestController
@RequestMapping("/api")
public class HopResource {

    private final Logger log = LoggerFactory.getLogger(HopResource.class);

    @Inject
    private HopRepository hopRepository;

    @Inject
    private HopMapper hopMapper;

    /**
     * POST  /hops -> Create a new hop.
     */
    @RequestMapping(value = "/hops",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HopDTO> createHop(@RequestBody HopDTO hopDTO) throws URISyntaxException {
        log.debug("REST request to save Hop : {}", hopDTO);
        if (hopDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hop cannot already have an ID").body(null);
        }
        Hop hop = hopMapper.hopDTOToHop(hopDTO);
        Hop result = hopRepository.save(hop);
        return ResponseEntity.created(new URI("/api/hops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hop", result.getId().toString()))
            .body(hopMapper.hopToHopDTO(result));
    }

    /**
     * PUT  /hops -> Updates an existing hop.
     */
    @RequestMapping(value = "/hops",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HopDTO> updateHop(@RequestBody HopDTO hopDTO) throws URISyntaxException {
        log.debug("REST request to update Hop : {}", hopDTO);
        if (hopDTO.getId() == null) {
            return createHop(hopDTO);
        }
        Hop hop = hopMapper.hopDTOToHop(hopDTO);
        Hop result = hopRepository.save(hop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hop", hopDTO.getId().toString()))
            .body(hopMapper.hopToHopDTO(result));
    }

    /**
     * GET  /hops -> get all the hops.
     */
    @RequestMapping(value = "/hops",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<HopDTO>> getAllHops(Pageable pageable)
        throws URISyntaxException {
        Page<Hop> page = hopRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hops");
        return new ResponseEntity<>(page.getContent().stream()
            .map(hopMapper::hopToHopDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /hops/:id -> get the "id" hop.
     */
    @RequestMapping(value = "/hops/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HopDTO> getHop(@PathVariable Long id) {
        log.debug("REST request to get Hop : {}", id);
        return Optional.ofNullable(hopRepository.findOne(id))
            .map(hopMapper::hopToHopDTO)
            .map(hopDTO -> new ResponseEntity<>(
                hopDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hops/:id -> delete the "id" hop.
     */
    @RequestMapping(value = "/hops/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHop(@PathVariable Long id) {
        log.debug("REST request to delete Hop : {}", id);
        hopRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hop", id.toString())).build();
    }
}
