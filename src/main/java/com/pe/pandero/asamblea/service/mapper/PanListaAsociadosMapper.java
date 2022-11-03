package com.pe.pandero.asamblea.service.mapper;

import com.pe.pandero.asamblea.domain.PanListaAsociados;
import com.pe.pandero.asamblea.service.dto.PanListaAsociadosDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link PanListaAsociados} and its DTO {@link PanListaAsociadosDTO}.
 */
@Mapper(componentModel = "spring")
public interface PanListaAsociadosMapper extends EntityMapper<PanListaAsociadosDTO, PanListaAsociados> {}
