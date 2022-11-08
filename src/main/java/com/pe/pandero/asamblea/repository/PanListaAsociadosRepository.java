package com.pe.pandero.asamblea.repository;

import com.pe.pandero.asamblea.domain.PanListaAsociados;
import javax.validation.constraints.Size;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the PanListaAsociados entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PanListaAsociadosRepository extends R2dbcRepository<PanListaAsociados, Long>, PanListaAsociadosRepositoryInternal {
    // just to avoid having unambigous methods
    @Override
    Flux<PanListaAsociados> findAll();

    @Override
    Mono<PanListaAsociados> findById(Long id);

    @Override
    <S extends PanListaAsociados> Mono<S> save(S entity);

    Flux<PanListaAsociados> getAllByPosicionId(Double posicionId);
}

interface PanListaAsociadosRepositoryInternal {
    <S extends PanListaAsociados> Mono<S> insert(S entity);
    <S extends PanListaAsociados> Mono<S> save(S entity);
    Mono<Integer> update(PanListaAsociados entity);

    Flux<PanListaAsociados> findAll();
    Mono<PanListaAsociados> findById(Long id);
    Flux<PanListaAsociados> findAllBy(Pageable pageable);
    Flux<PanListaAsociados> findAllBy(Pageable pageable, Criteria criteria);
}
