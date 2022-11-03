package com.pe.pandero.asamblea.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PanListaAsociadosMapperTest {

    private PanListaAsociadosMapper panListaAsociadosMapper;

    @BeforeEach
    public void setUp() {
        panListaAsociadosMapper = new PanListaAsociadosMapperImpl();
    }
}
