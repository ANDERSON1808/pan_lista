package com.pe.pandero.asamblea.repository.rowmapper;

import com.pe.pandero.asamblea.domain.Balotas;
import com.pe.pandero.asamblea.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Balotas}, with proper type conversions.
 */
@Service
public class BalotasRowMapper implements BiFunction<Row, String, Balotas> {

    private final ColumnConverter converter;

    public BalotasRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Balotas} stored in the database.
     */
    @Override
    public Balotas apply(Row row, String prefix) {
        Balotas entity = new Balotas();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNumero(converter.fromRow(row, prefix + "_numero", Float.class));
        entity.setCreado(converter.fromRow(row, prefix + "_creado", Instant.class));
        entity.setSorteosId(converter.fromRow(row, prefix + "_sorteos_id", Long.class));
        entity.setPanListaAsociadosId(converter.fromRow(row, prefix + "_pan_lista_asociados_id", Long.class));
        return entity;
    }
}
