package com.pe.pandero.asamblea.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.pe.pandero.asamblea.domain.Balotas;
import com.pe.pandero.asamblea.repository.rowmapper.BalotasRowMapper;
import com.pe.pandero.asamblea.repository.rowmapper.PanListaAsociadosRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Balotas entity.
 */
@SuppressWarnings("unused")
class BalotasRepositoryInternalImpl implements BalotasRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SorteosRowMapper sorteosMapper;
    private final PanListaAsociadosRowMapper panlistaasociadosMapper;
    private final BalotasRowMapper balotasMapper;

    private static final Table entityTable = Table.aliased("balotas", EntityManager.ENTITY_ALIAS);
    private static final Table sorteosTable = Table.aliased("sorteos", "sorteos");
    private static final Table panListaAsociadosTable = Table.aliased("pan_lista_asociados", "panListaAsociados");

    public BalotasRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SorteosRowMapper sorteosMapper,
        PanListaAsociadosRowMapper panlistaasociadosMapper,
        BalotasRowMapper balotasMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sorteosMapper = sorteosMapper;
        this.panlistaasociadosMapper = panlistaasociadosMapper;
        this.balotasMapper = balotasMapper;
    }

    @Override
    public Flux<Balotas> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Balotas> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Balotas> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = BalotasSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SorteosSqlHelper.getColumns(sorteosTable, "sorteos"));
        columns.addAll(PanListaAsociadosSqlHelper.getColumns(panListaAsociadosTable, "panListaAsociados"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(sorteosTable)
            .on(Column.create("sorteos_id", entityTable))
            .equals(Column.create("id", sorteosTable))
            .leftOuterJoin(panListaAsociadosTable)
            .on(Column.create("pan_lista_asociados_id", entityTable))
            .equals(Column.create("id", panListaAsociadosTable));

        String select = entityManager.createSelect(selectFrom, Balotas.class, pageable, criteria);
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
    public Flux<Balotas> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Balotas> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Balotas process(Row row, RowMetadata metadata) {
        Balotas entity = balotasMapper.apply(row, "e");
        entity.setSorteos(sorteosMapper.apply(row, "sorteos"));
        entity.setPanListaAsociados(panlistaasociadosMapper.apply(row, "panListaAsociados"));
        return entity;
    }

    @Override
    public <S extends Balotas> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Balotas> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Balotas with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Balotas entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class BalotasSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("numero", table, columnPrefix + "_numero"));
        columns.add(Column.aliased("creado", table, columnPrefix + "_creado"));

        columns.add(Column.aliased("sorteos_id", table, columnPrefix + "_sorteos_id"));
        columns.add(Column.aliased("pan_lista_asociados_id", table, columnPrefix + "_pan_lista_asociados_id"));
        return columns;
    }
}
