package org.dmarkowski.brewnote.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.dmarkowski.brewnote.domain.Additional;
import org.dmarkowski.brewnote.repository.AdditionalRepository;
import org.dmarkowski.brewnote.web.rest.util.HeaderUtil;
import org.dmarkowski.brewnote.web.rest.util.PaginationUtil;
import org.dmarkowski.brewnote.web.rest.dto.AdditionalDTO;
import org.dmarkowski.brewnote.web.rest.mapper.AdditionalMapper;
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
 * REST controller for managing Additional.
 */
@RestController
@RequestMapping("/api")
public class AdditionalResource {

    private final Logger log = LoggerFactory.getLogger(AdditionalResource.class);

    @Inject
    private AdditionalRepository additionalRepository;

    @Inject
    private AdditionalMapper additionalMapper;

    /**
     * POST  /additionals -> Create a new additional.
     */
    @RequestMapping(value = "/additionals",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdditionalDTO> createAdditional(@RequestBody AdditionalDTO additionalDTO) throws URISyntaxException {
        log.debug("REST request to save Additional : {}", additionalDTO);
        if (additionalDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new additional cannot already have an ID").body(null);
        }
        Additional additional = additionalMapper.additionalDTOToAdditional(additionalDTO);
        Additional result = additionalRepository.save(additional);
        return ResponseEntity.created(new URI("/api/additionals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("additional", result.getId().toString()))
            .body(additionalMapper.additionalToAdditionalDTO(result));
    }

    /**
     * PUT  /additionals -> Updates an existing additional.
     */
    @RequestMapping(value = "/additionals",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdditionalDTO> updateAdditional(@RequestBody AdditionalDTO additionalDTO) throws URISyntaxException {
        log.debug("REST request to update Additional : {}", additionalDTO);
        if (additionalDTO.getId() == null) {
            return createAdditional(additionalDTO);
        }
        Additional additional = additionalMapper.additionalDTOToAdditional(additionalDTO);
        Additional result = additionalRepository.save(additional);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("additional", additionalDTO.getId().toString()))
            .body(additionalMapper.additionalToAdditionalDTO(result));
    }

    /**
     * GET  /additionals -> get all the additionals.
     */
    @RequestMapping(value = "/additionals",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AdditionalDTO>> getAllAdditionals(Pageable pageable)
        throws URISyntaxException {
        Page<Additional> page = additionalRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/additionals");
        return new ResponseEntity<>(page.getContent().stream()
            .map(additionalMapper::additionalToAdditionalDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /additionals/:id -> get the "id" additional.
     */
    @RequestMapping(value = "/additionals/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdditionalDTO> getAdditional(@PathVariable Long id) {
        log.debug("REST request to get Additional : {}", id);
        return Optional.ofNullable(additionalRepository.findOne(id))
            .map(additionalMapper::additionalToAdditionalDTO)
            .map(additionalDTO -> new ResponseEntity<>(
                additionalDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /additionals/:id -> delete the "id" additional.
     */
    @RequestMapping(value = "/additionals/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdditional(@PathVariable Long id) {
        log.debug("REST request to delete Additional : {}", id);
        additionalRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("additional", id.toString())).build();
    }
}
