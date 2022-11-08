package com.pe.pandero.asamblea.web.rest.vm;

import com.pe.pandero.asamblea.service.dto.PanListaAsociadosDTO;

public class SorteoVM {

    private final Integer orden;
    private final PanListaAsociadosDTO asociados;

    public Integer getOrden() {
        return orden;
    }

    public PanListaAsociadosDTO getAsociados() {
        return asociados;
    }

    public SorteoVM(Integer orden, PanListaAsociadosDTO asociados) {
        this.orden = orden;
        this.asociados = asociados;
    }
}
