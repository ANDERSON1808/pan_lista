package com.pe.pandero.asamblea.service;

import com.pe.pandero.asamblea.domain.Balotas;
import com.pe.pandero.asamblea.repository.BalotasRepository;
import com.pe.pandero.asamblea.service.dto.BalotasDTO;
import com.pe.pandero.asamblea.service.mapper.BalotasMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Balotas}.
 */
@Service
@Transactional
public class BalotasService {

    private final Logger log = LoggerFactory.getLogger(BalotasService.class);

    private final BalotasRepository balotasRepository;

    private final BalotasMapper balotasMapper;

    public BalotasService(BalotasRepository balotasRepository, BalotasMapper balotasMapper) {
        this.balotasRepository = balotasRepository;
        this.balotasMapper = balotasMapper;
    }

    /**
     * Save a balotas.
     *
     * @param balotasDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BalotasDTO> save(BalotasDTO balotasDTO) {
        log.debug("Request to save Balotas : {}", balotasDTO);
        return balotasRepository.save(balotasMapper.toEntity(balotasDTO)).map(balotasMapper::toDto);
    }

    /**
     * Partially update a balotas.
     *
     * @param balotasDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<BalotasDTO> partialUpdate(BalotasDTO balotasDTO) {
        log.debug("Request to partially update Balotas : {}", balotasDTO);

        return balotasRepository
            .findById(balotasDTO.getId())
            .map(
                existingBalotas -> {
                    balotasMapper.partialUpdate(existingBalotas, balotasDTO);

                    return existingBalotas;
                }
            )
            .flatMap(balotasRepository::save)
            .map(balotasMapper::toDto);
    }

    /**
     * Get all the balotas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<BalotasDTO> findAll() {
        log.debug("Request to get all Balotas");
        return balotasRepository.findAll().map(balotasMapper::toDto);
    }

    /**
     * Returns the number of balotas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return balotasRepository.count();
    }

    /**
     * Get one balotas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<BalotasDTO> findOne(Long id) {
        log.debug("Request to get Balotas : {}", id);
        return balotasRepository.findById(id).map(balotasMapper::toDto);
    }

    /**
     * Delete the balotas by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Balotas : {}", id);
        return balotasRepository.deleteById(id);
    }
}
