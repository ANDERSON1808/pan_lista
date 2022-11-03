package com.pe.pandero.asamblea.service.mapper;

import com.pe.pandero.asamblea.domain.*;
import com.pe.pandero.asamblea.service.dto.BalotasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Balotas} and its DTO {@link BalotasDTO}.
 */
@Mapper(componentModel = "spring", uses = { SorteosMapper.class, PanListaAsociadosMapper.class })
public interface BalotasMapper extends EntityMapper<BalotasDTO, Balotas> {
    @Mapping(target = "sorteos", source = "sorteos", qualifiedByName = "id")
    @Mapping(target = "panListaAsociados", source = "panListaAsociados", qualifiedByName = "id")
    BalotasDTO toDto(Balotas s);
}
