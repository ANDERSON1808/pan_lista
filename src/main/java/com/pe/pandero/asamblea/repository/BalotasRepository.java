package com.pe.pandero.asamblea.repository;

import com.pe.pandero.asamblea.domain.Balotas;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Balotas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BalotasRepository extends R2dbcRepository<Balotas, Long>, BalotasRepositoryInternal {
    @Query("SELECT * FROM balotas entity WHERE entity.sorteos_id = :id")
    Flux<Balotas> findBySorteos(Long id);

    @Query("SELECT * FROM balotas entity WHERE entity.sorteos_id IS NULL")
    Flux<Balotas> findAllWhereSorteosIsNull();

    @Query("SELECT * FROM balotas entity WHERE entity.pan_lista_asociados_id = :id")
    Flux<Balotas> findByPanListaAsociados(Long id);

    @Query("SELECT * FROM balotas entity WHERE entity.pan_lista_asociados_id IS NULL")
    Flux<Balotas> findAllWherePanListaAsociadosIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Balotas> findAll();

    @Override
    Mono<Balotas> findById(Long id);

    @Override
    <S extends Balotas> Mono<S> save(S entity);
}

interface BalotasRepositoryInternal {
    <S extends Balotas> Mono<S> insert(S entity);
    <S extends Balotas> Mono<S> save(S entity);
    Mono<Integer> update(Balotas entity);

    Flux<Balotas> findAll();
    Mono<Balotas> findById(Long id);
    Flux<Balotas> findAllBy(Pageable pageable);
    Flux<Balotas> findAllBy(Pageable pageable, Criteria criteria);
}
