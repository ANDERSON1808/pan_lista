package com.pe.pandero.asamblea.service;

import com.pe.pandero.asamblea.domain.PanListaAsociados;
import com.pe.pandero.asamblea.repository.PanListaAsociadosRepository;
import com.pe.pandero.asamblea.service.dto.PanListaAsociadosDTO;
import com.pe.pandero.asamblea.service.mapper.PanListaAsociadosMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link PanListaAsociados}.
 */
@Service
@Transactional
public class PanListaAsociadosService {

    private final Logger log = LoggerFactory.getLogger(PanListaAsociadosService.class);

    private final PanListaAsociadosRepository panListaAsociadosRepository;

    private final PanListaAsociadosMapper panListaAsociadosMapper;

    public PanListaAsociadosService(
        PanListaAsociadosRepository panListaAsociadosRepository,
        PanListaAsociadosMapper panListaAsociadosMapper
    ) {
        this.panListaAsociadosRepository = panListaAsociadosRepository;
        this.panListaAsociadosMapper = panListaAsociadosMapper;
    }

    /**
     * Save a panListaAsociados.
     *
     * @param panListaAsociadosDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PanListaAsociadosDTO> save(PanListaAsociadosDTO panListaAsociadosDTO) {
        log.debug("Request to save PanListaAsociados : {}", panListaAsociadosDTO);
        return panListaAsociadosRepository.save(panListaAsociadosMapper.toEntity(panListaAsociadosDTO)).map(panListaAsociadosMapper::toDto);
    }

    /**
     * Partially update a panListaAsociados.
     *
     * @param panListaAsociadosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PanListaAsociadosDTO> partialUpdate(PanListaAsociadosDTO panListaAsociadosDTO) {
        log.debug("Request to partially update PanListaAsociados : {}", panListaAsociadosDTO);

        return panListaAsociadosRepository
            .findById(panListaAsociadosDTO.getId())
            .map(
                existingPanListaAsociados -> {
                    panListaAsociadosMapper.partialUpdate(existingPanListaAsociados, panListaAsociadosDTO);

                    return existingPanListaAsociados;
                }
            )
            .flatMap(panListaAsociadosRepository::save)
            .map(panListaAsociadosMapper::toDto);
    }

    /**
     * Get all the panListaAsociados.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PanListaAsociadosDTO> findAll() {
        log.debug("Request to get all PanListaAsociados");
        return panListaAsociadosRepository.findAll().map(panListaAsociadosMapper::toDto);
    }

    /**
     * Returns the number of panListaAsociados available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return panListaAsociadosRepository.count();
    }

    /**
     * Get one panListaAsociados by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PanListaAsociadosDTO> findOne(Long id) {
        log.debug("Request to get PanListaAsociados : {}", id);
        return panListaAsociadosRepository.findById(id).map(panListaAsociadosMapper::toDto);
    }

    /**
     * Delete the panListaAsociados by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PanListaAsociados : {}", id);
        return panListaAsociadosRepository.deleteById(id);
    }

    public Flux<PanListaAsociadosDTO> findByPosicionIdAndEstadoHabil(Double posicionId) {
        return panListaAsociadosRepository.getAllByPosicionId(posicionId).map(panListaAsociadosMapper::toDto);
    }
}
