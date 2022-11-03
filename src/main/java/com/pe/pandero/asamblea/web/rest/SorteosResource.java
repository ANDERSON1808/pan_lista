package com.pe.pandero.asamblea.web.rest;

import com.pe.pandero.asamblea.repository.SorteosRepository;
import com.pe.pandero.asamblea.service.SorteosService;
import com.pe.pandero.asamblea.service.dto.SorteosDTO;
import com.pe.pandero.asamblea.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.pe.pandero.asamblea.domain.Sorteos}.
 */
@RestController
@RequestMapping("/api")
public class SorteosResource {

    private final Logger log = LoggerFactory.getLogger(SorteosResource.class);

    private static final String ENTITY_NAME = "sorteos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SorteosService sorteosService;

    private final SorteosRepository sorteosRepository;

    public SorteosResource(SorteosService sorteosService, SorteosRepository sorteosRepository) {
        this.sorteosService = sorteosService;
        this.sorteosRepository = sorteosRepository;
    }

    /**
     * {@code POST  /sorteos} : Create a new sorteos.
     *
     * @param sorteosDTO the sorteosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sorteosDTO, or with status {@code 400 (Bad Request)} if the sorteos has already an ID.
     */
    @PostMapping("/sorteos")
    public Mono<ResponseEntity<SorteosDTO>> createSorteos(@RequestBody SorteosDTO sorteosDTO) {
        log.debug("REST request to save Sorteos : {}", sorteosDTO);
        if (sorteosDTO.getId() != null) {
            throw new BadRequestAlertException("A new sorteos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sorteosService
            .save(sorteosDTO)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/sorteos/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /sorteos/:id} : Updates an existing sorteos.
     *
     * @param id the id of the sorteosDTO to save.
     * @param sorteosDTO the sorteosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sorteosDTO,
     * or with status {@code 400 (Bad Request)} if the sorteosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sorteosDTO couldn't be updated.
     */
    @PutMapping("/sorteos/{id}")
    public Mono<ResponseEntity<SorteosDTO>> updateSorteos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SorteosDTO sorteosDTO
    ) {
        log.debug("REST request to update Sorteos : {}, {}", id, sorteosDTO);
        if (sorteosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sorteosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sorteosRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return sorteosService
                        .save(sorteosDTO)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            result ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString())
                                    )
                                    .body(result)
                        );
                }
            );
    }

    /**
     * {@code PATCH  /sorteos/:id} : Partial updates given fields of an existing sorteos, field will ignore if it is null
     *
     * @param id the id of the sorteosDTO to save.
     * @param sorteosDTO the sorteosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sorteosDTO,
     * or with status {@code 400 (Bad Request)} if the sorteosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sorteosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sorteosDTO couldn't be updated.
     */
    @PatchMapping(value = "/sorteos/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<SorteosDTO>> partialUpdateSorteos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SorteosDTO sorteosDTO
    ) {
        log.debug("REST request to partial update Sorteos partially : {}, {}", id, sorteosDTO);
        if (sorteosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sorteosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sorteosRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<SorteosDTO> result = sorteosService.partialUpdate(sorteosDTO);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            res ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString())
                                    )
                                    .body(res)
                        );
                }
            );
    }

    /**
     * {@code GET  /sorteos} : get all the sorteos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sorteos in body.
     */
    @GetMapping("/sorteos")
    public Mono<List<SorteosDTO>> getAllSorteos() {
        log.debug("REST request to get all Sorteos");
        return sorteosService.findAll().collectList();
    }

    /**
     * {@code GET  /sorteos} : get all the sorteos as a stream.
     * @return the {@link Flux} of sorteos.
     */
    @GetMapping(value = "/sorteos", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SorteosDTO> getAllSorteosAsStream() {
        log.debug("REST request to get all Sorteos as a stream");
        return sorteosService.findAll();
    }

    /**
     * {@code GET  /sorteos/:id} : get the "id" sorteos.
     *
     * @param id the id of the sorteosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sorteosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sorteos/{id}")
    public Mono<ResponseEntity<SorteosDTO>> getSorteos(@PathVariable Long id) {
        log.debug("REST request to get Sorteos : {}", id);
        Mono<SorteosDTO> sorteosDTO = sorteosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sorteosDTO);
    }

    /**
     * {@code DELETE  /sorteos/:id} : delete the "id" sorteos.
     *
     * @param id the id of the sorteosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sorteos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSorteos(@PathVariable Long id) {
        log.debug("REST request to delete Sorteos : {}", id);
        return sorteosService
            .delete(id)
            .map(
                result ->
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
            );
    }
}
