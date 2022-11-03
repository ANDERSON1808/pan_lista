package com.pe.pandero.asamblea.web.rest;

import com.pe.pandero.asamblea.repository.BalotasRepository;
import com.pe.pandero.asamblea.service.BalotasService;
import com.pe.pandero.asamblea.service.dto.BalotasDTO;
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
 * REST controller for managing {@link com.pe.pandero.asamblea.domain.Balotas}.
 */
@RestController
@RequestMapping("/api")
public class BalotasResource {

    private final Logger log = LoggerFactory.getLogger(BalotasResource.class);

    private static final String ENTITY_NAME = "balotas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BalotasService balotasService;

    private final BalotasRepository balotasRepository;

    public BalotasResource(BalotasService balotasService, BalotasRepository balotasRepository) {
        this.balotasService = balotasService;
        this.balotasRepository = balotasRepository;
    }

    /**
     * {@code POST  /balotas} : Create a new balotas.
     *
     * @param balotasDTO the balotasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new balotasDTO, or with status {@code 400 (Bad Request)} if the balotas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/balotas")
    public Mono<ResponseEntity<BalotasDTO>> createBalotas(@RequestBody BalotasDTO balotasDTO) throws URISyntaxException {
        log.debug("REST request to save Balotas : {}", balotasDTO);
        if (balotasDTO.getId() != null) {
            throw new BadRequestAlertException("A new balotas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return balotasService
            .save(balotasDTO)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/balotas/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /balotas/:id} : Updates an existing balotas.
     *
     * @param id the id of the balotasDTO to save.
     * @param balotasDTO the balotasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated balotasDTO,
     * or with status {@code 400 (Bad Request)} if the balotasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the balotasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/balotas/{id}")
    public Mono<ResponseEntity<BalotasDTO>> updateBalotas(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BalotasDTO balotasDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Balotas : {}, {}", id, balotasDTO);
        if (balotasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, balotasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return balotasRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return balotasService
                        .save(balotasDTO)
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
     * {@code PATCH  /balotas/:id} : Partial updates given fields of an existing balotas, field will ignore if it is null
     *
     * @param id the id of the balotasDTO to save.
     * @param balotasDTO the balotasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated balotasDTO,
     * or with status {@code 400 (Bad Request)} if the balotasDTO is not valid,
     * or with status {@code 404 (Not Found)} if the balotasDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the balotasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/balotas/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<BalotasDTO>> partialUpdateBalotas(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BalotasDTO balotasDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Balotas partially : {}, {}", id, balotasDTO);
        if (balotasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, balotasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return balotasRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<BalotasDTO> result = balotasService.partialUpdate(balotasDTO);

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
     * {@code GET  /balotas} : get all the balotas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of balotas in body.
     */
    @GetMapping("/balotas")
    public Mono<List<BalotasDTO>> getAllBalotas() {
        log.debug("REST request to get all Balotas");
        return balotasService.findAll().collectList();
    }

    /**
     * {@code GET  /balotas} : get all the balotas as a stream.
     * @return the {@link Flux} of balotas.
     */
    @GetMapping(value = "/balotas", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<BalotasDTO> getAllBalotasAsStream() {
        log.debug("REST request to get all Balotas as a stream");
        return balotasService.findAll();
    }

    /**
     * {@code GET  /balotas/:id} : get the "id" balotas.
     *
     * @param id the id of the balotasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the balotasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/balotas/{id}")
    public Mono<ResponseEntity<BalotasDTO>> getBalotas(@PathVariable Long id) {
        log.debug("REST request to get Balotas : {}", id);
        Mono<BalotasDTO> balotasDTO = balotasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(balotasDTO);
    }

    /**
     * {@code DELETE  /balotas/:id} : delete the "id" balotas.
     *
     * @param id the id of the balotasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/balotas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBalotas(@PathVariable Long id) {
        log.debug("REST request to delete Balotas : {}", id);
        return balotasService
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
