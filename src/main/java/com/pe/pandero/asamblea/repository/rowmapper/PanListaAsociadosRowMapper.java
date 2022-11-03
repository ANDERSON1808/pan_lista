package com.pe.pandero.asamblea.repository.rowmapper;

import com.pe.pandero.asamblea.domain.PanListaAsociados;
import com.pe.pandero.asamblea.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PanListaAsociados}, with proper type conversions.
 */
@Service
public class PanListaAsociadosRowMapper implements BiFunction<Row, String, PanListaAsociados> {

    private final ColumnConverter converter;

    public PanListaAsociadosRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PanListaAsociados} stored in the database.
     */
    @Override
    public PanListaAsociados apply(Row row, String prefix) {
        PanListaAsociados entity = new PanListaAsociados();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setListaAsociadosId(converter.fromRow(row, prefix + "_lista_asociados_id", Integer.class));
        entity.setGrupo(converter.fromRow(row, prefix + "_grupo", Double.class));
        entity.setPosicionId(converter.fromRow(row, prefix + "_posicion_id", Double.class));
        entity.setNombreCompleto(converter.fromRow(row, prefix + "_nombre_completo", String.class));
        entity.setEstadoHabil(converter.fromRow(row, prefix + "_estado_habil", String.class));
        return entity;
    }
}
