package com.pe.pandero.asamblea.service;

import com.pe.pandero.asamblea.domain.Sorteos;
import com.pe.pandero.asamblea.repository.SorteosRepository;
import com.pe.pandero.asamblea.service.dto.SorteosDTO;
import com.pe.pandero.asamblea.service.mapper.SorteosMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Sorteos}.
 */
@Service
@Transactional
public class SorteosService {

    private final Logger log = LoggerFactory.getLogger(SorteosService.class);

    private final SorteosRepository sorteosRepository;

    private final SorteosMapper sorteosMapper;

    public SorteosService(SorteosRepository sorteosRepository, SorteosMapper sorteosMapper) {
        this.sorteosRepository = sorteosRepository;
        this.sorteosMapper = sorteosMapper;
    }

    /**
     * Save a sorteos.
     *
     * @param sorteosDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SorteosDTO> save(SorteosDTO sorteosDTO) {
        log.debug("Request to save Sorteos : {}", sorteosDTO);
        return sorteosRepository.save(sorteosMapper.toEntity(sorteosDTO)).map(sorteosMapper::toDto);
    }

    /**
     * Partially update a sorteos.
     *
     * @param sorteosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SorteosDTO> partialUpdate(SorteosDTO sorteosDTO) {
        log.debug("Request to partially update Sorteos : {}", sorteosDTO);

        return sorteosRepository
            .findById(sorteosDTO.getId())
            .map(
                existingSorteos -> {
                    sorteosMapper.partialUpdate(existingSorteos, sorteosDTO);

                    return existingSorteos;
                }
            )
            .flatMap(sorteosRepository::save)
            .map(sorteosMapper::toDto);
    }

    /**
     * Get all the sorteos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SorteosDTO> findAll() {
        log.debug("Request to get all Sorteos");
        return sorteosRepository.findAll().map(sorteosMapper::toDto);
    }

    /**
     * Returns the number of sorteos available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return sorteosRepository.count();
    }

    /**
     * Get one sorteos by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SorteosDTO> findOne(Long id) {
        log.debug("Request to get Sorteos : {}", id);
        return sorteosRepository.findById(id).map(sorteosMapper::toDto);
    }

    /**
     * Delete the sorteos by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Sorteos : {}", id);
        return sorteosRepository.deleteById(id);
    }
}
