package com.pe.pandero.asamblea.service.mapper;

import com.pe.pandero.asamblea.domain.PanListaAsociados;
import com.pe.pandero.asamblea.service.dto.PanListaAsociadosDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link PanListaAsociados} and its DTO {@link PanListaAsociadosDTO}.
 */
@Mapper(componentModel = "spring")
public interface PanListaAsociadosMapper extends EntityMapper<PanListaAsociadosDTO, PanListaAsociados> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PanListaAsociadosDTO toDtoId(PanListaAsociados panListaAsociados);
}
