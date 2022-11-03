package com.pe.pandero.asamblea.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.pe.pandero.asamblea.domain.PanListaAsociados;
import com.pe.pandero.asamblea.repository.rowmapper.PanListaAsociadosRowMapper;
import com.pe.pandero.asamblea.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
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
 * Spring Data SQL reactive custom repository implementation for the PanListaAsociados entity.
 */
@SuppressWarnings("unused")
class PanListaAsociadosRepositoryInternalImpl implements PanListaAsociadosRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PanListaAsociadosRowMapper panlistaasociadosMapper;

    private static final Table entityTable = Table.aliased("pan_lista_asociados", EntityManager.ENTITY_ALIAS);

    public PanListaAsociadosRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PanListaAsociadosRowMapper panlistaasociadosMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.panlistaasociadosMapper = panlistaasociadosMapper;
    }

    @Override
    public Flux<PanListaAsociados> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<PanListaAsociados> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<PanListaAsociados> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = PanListaAsociadosSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, PanListaAsociados.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(
                crit ->
                    new StringBuilder(select)
                        .append(" ")
                        .append("WHERE")
                        .append(" ")
                        .append(alias)
                        .append(".")
                        .append(crit.toString())
                        .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<PanListaAsociados> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<PanListaAsociados> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private PanListaAsociados process(Row row, RowMetadata metadata) {
        PanListaAsociados entity = panlistaasociadosMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends PanListaAsociados> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends PanListaAsociados> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update PanListaAsociados with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(PanListaAsociados entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class PanListaAsociadosSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("lista_asociados_id", table, columnPrefix + "_lista_asociados_id"));
        columns.add(Column.aliased("grupo", table, columnPrefix + "_grupo"));
        columns.add(Column.aliased("posicion_id", table, columnPrefix + "_posicion_id"));
        columns.add(Column.aliased("nombre_completo", table, columnPrefix + "_nombre_completo"));
        columns.add(Column.aliased("estado_habil", table, columnPrefix + "_estado_habil"));

        return columns;
    }
}
