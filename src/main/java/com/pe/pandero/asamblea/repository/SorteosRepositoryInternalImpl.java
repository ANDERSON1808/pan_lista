package com.pe.pandero.asamblea.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.pe.pandero.asamblea.domain.Sorteos;
import com.pe.pandero.asamblea.repository.rowmapper.SorteosRowMapper;
import com.pe.pandero.asamblea.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Sorteos entity.
 */
@SuppressWarnings("unused")
class SorteosRepositoryInternalImpl implements SorteosRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SorteosRowMapper sorteosMapper;

    private static final Table entityTable = Table.aliased("sorteos", EntityManager.ENTITY_ALIAS);

    public SorteosRepositoryInternalImpl(R2dbcEntityTemplate template, EntityManager entityManager, SorteosRowMapper sorteosMapper) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sorteosMapper = sorteosMapper;
    }

    @Override
    public Flux<Sorteos> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Sorteos> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Sorteos> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = SorteosSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, Sorteos.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(
                crit -> new StringBuilder(select).append(" ").append("WHERE").append(" ").append(alias).append(".").append(crit).toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<Sorteos> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Sorteos> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Sorteos process(Row row, RowMetadata metadata) {
        return sorteosMapper.apply(row, "e");
    }

    @Override
    public <S extends Sorteos> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Sorteos> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates <= 0) {
                            throw new IllegalStateException("Unable to update Sorteos with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Sorteos entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class SorteosSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("fecha_sorteo", table, columnPrefix + "_fecha_sorteo"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));

        return columns;
    }
}
