package com.pe.pandero.asamblea.repository.rowmapper;

import com.pe.pandero.asamblea.domain.Sorteos;
import com.pe.pandero.asamblea.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Sorteos}, with proper type conversions.
 */
@Service
public class SorteosRowMapper implements BiFunction<Row, String, Sorteos> {

    private final ColumnConverter converter;

    public SorteosRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Sorteos} stored in the database.
     */
    @Override
    public Sorteos apply(Row row, String prefix) {
        Sorteos entity = new Sorteos();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFechaSorteo(converter.fromRow(row, prefix + "_fecha_sorteo", Instant.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        return entity;
    }
}
