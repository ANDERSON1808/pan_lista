package com.pe.pandero.asamblea.repository;

import com.pe.pandero.asamblea.domain.Sorteos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Sorteos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SorteosRepository extends R2dbcRepository<Sorteos, Long>, SorteosRepositoryInternal {
    // just to avoid having unambigous methods
    @Override
    Flux<Sorteos> findAll();

    @Override
    Mono<Sorteos> findById(Long id);

    @Override
    <S extends Sorteos> Mono<S> save(S entity);
}

interface SorteosRepositoryInternal {
    <S extends Sorteos> Mono<S> insert(S entity);
    <S extends Sorteos> Mono<S> save(S entity);
    Mono<Integer> update(Sorteos entity);

    Flux<Sorteos> findAll();
    Mono<Sorteos> findById(Long id);
    Flux<Sorteos> findAllBy(Pageable pageable);
    Flux<Sorteos> findAllBy(Pageable pageable, Criteria criteria);
}
