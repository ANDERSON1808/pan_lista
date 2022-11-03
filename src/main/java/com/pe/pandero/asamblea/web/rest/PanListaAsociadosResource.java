package com.pe.pandero.asamblea.web.rest;

import com.pe.pandero.asamblea.repository.PanListaAsociadosRepository;
import com.pe.pandero.asamblea.service.PanListaAsociadosService;
import com.pe.pandero.asamblea.service.dto.PanListaAsociadosDTO;
import com.pe.pandero.asamblea.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.pe.pandero.asamblea.domain.PanListaAsociados}.
 */
@RestController
@RequestMapping("/api")
public class PanListaAsociadosResource {

    private final Logger log = LoggerFactory.getLogger(PanListaAsociadosResource.class);

    private static final String ENTITY_NAME = "panListaAsociados";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PanListaAsociadosService panListaAsociadosService;

    private final PanListaAsociadosRepository panListaAsociadosRepository;

    public PanListaAsociadosResource(
        PanListaAsociadosService panListaAsociadosService,
        PanListaAsociadosRepository panListaAsociadosRepository
    ) {
        this.panListaAsociadosService = panListaAsociadosService;
        this.panListaAsociadosRepository = panListaAsociadosRepository;
    }

    /**
     * {@code POST  /pan-lista-asociados} : Create a new panListaAsociados.
     *
     * @param panListaAsociadosDTO the panListaAsociadosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new panListaAsociadosDTO, or with status {@code 400 (Bad Request)} if the panListaAsociados has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pan-lista-asociados")
    public Mono<ResponseEntity<PanListaAsociadosDTO>> createPanListaAsociados(
        @Valid @RequestBody PanListaAsociadosDTO panListaAsociadosDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PanListaAsociados : {}", panListaAsociadosDTO);
        if (panListaAsociadosDTO.getId() != null) {
            throw new BadRequestAlertException("A new panListaAsociados cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return panListaAsociadosService
            .save(panListaAsociadosDTO)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/pan-lista-asociados/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /pan-lista-asociados/:id} : Updates an existing panListaAsociados.
     *
     * @param id the id of the panListaAsociadosDTO to save.
     * @param panListaAsociadosDTO the panListaAsociadosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panListaAsociadosDTO,
     * or with status {@code 400 (Bad Request)} if the panListaAsociadosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the panListaAsociadosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pan-lista-asociados/{id}")
    public Mono<ResponseEntity<PanListaAsociadosDTO>> updatePanListaAsociados(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PanListaAsociadosDTO panListaAsociadosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PanListaAsociados : {}, {}", id, panListaAsociadosDTO);
        if (panListaAsociadosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, panListaAsociadosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return panListaAsociadosRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return panListaAsociadosService
                        .save(panListaAsociadosDTO)
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
     * {@code PATCH  /pan-lista-asociados/:id} : Partial updates given fields of an existing panListaAsociados, field will ignore if it is null
     *
     * @param id the id of the panListaAsociadosDTO to save.
     * @param panListaAsociadosDTO the panListaAsociadosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panListaAsociadosDTO,
     * or with status {@code 400 (Bad Request)} if the panListaAsociadosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the panListaAsociadosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the panListaAsociadosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pan-lista-asociados/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<PanListaAsociadosDTO>> partialUpdatePanListaAsociados(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PanListaAsociadosDTO panListaAsociadosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PanListaAsociados partially : {}, {}", id, panListaAsociadosDTO);
        if (panListaAsociadosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, panListaAsociadosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return panListaAsociadosRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<PanListaAsociadosDTO> result = panListaAsociadosService.partialUpdate(panListaAsociadosDTO);

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
     * {@code GET  /pan-lista-asociados} : get all the panListaAsociados.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of panListaAsociados in body.
     */
    @GetMapping("/pan-lista-asociados")
    public Mono<List<PanListaAsociadosDTO>> getAllPanListaAsociados() {
        log.debug("REST request to get all PanListaAsociados");
        return panListaAsociadosService.findAll().collectList();
    }

    /**
     * {@code GET  /pan-lista-asociados} : get all the panListaAsociados as a stream.
     * @return the {@link Flux} of panListaAsociados.
     */
    @GetMapping(value = "/pan-lista-asociados", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PanListaAsociadosDTO> getAllPanListaAsociadosAsStream() {
        log.debug("REST request to get all PanListaAsociados as a stream");
        return panListaAsociadosService.findAll();
    }

    /**
     * {@code GET  /pan-lista-asociados/:id} : get the "id" panListaAsociados.
     *
     * @param id the id of the panListaAsociadosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the panListaAsociadosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pan-lista-asociados/{id}")
    public Mono<ResponseEntity<PanListaAsociadosDTO>> getPanListaAsociados(@PathVariable Long id) {
        log.debug("REST request to get PanListaAsociados : {}", id);
        Mono<PanListaAsociadosDTO> panListaAsociadosDTO = panListaAsociadosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(panListaAsociadosDTO);
    }

    /**
     * {@code DELETE  /pan-lista-asociados/:id} : delete the "id" panListaAsociados.
     *
     * @param id the id of the panListaAsociadosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pan-lista-asociados/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePanListaAsociados(@PathVariable Long id) {
        log.debug("REST request to delete PanListaAsociados : {}", id);
        return panListaAsociadosService
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
