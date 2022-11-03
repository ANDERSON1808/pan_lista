package com.pe.pandero.asamblea.service.mapper;

import com.pe.pandero.asamblea.domain.Sorteos;
import com.pe.pandero.asamblea.service.dto.SorteosDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Sorteos} and its DTO {@link SorteosDTO}.
 */
@Mapper(componentModel = "spring")
public interface SorteosMapper extends EntityMapper<SorteosDTO, Sorteos> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SorteosDTO toDtoId(Sorteos sorteos);
}
