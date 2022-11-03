package com.pe.pandero.asamblea.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pe.pandero.asamblea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PanListaAsociadosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PanListaAsociadosDTO.class);
        PanListaAsociadosDTO panListaAsociadosDTO1 = new PanListaAsociadosDTO();
        panListaAsociadosDTO1.setId(1L);
        PanListaAsociadosDTO panListaAsociadosDTO2 = new PanListaAsociadosDTO();
        assertThat(panListaAsociadosDTO1).isNotEqualTo(panListaAsociadosDTO2);
        panListaAsociadosDTO2.setId(panListaAsociadosDTO1.getId());
        assertThat(panListaAsociadosDTO1).isEqualTo(panListaAsociadosDTO2);
        panListaAsociadosDTO2.setId(2L);
        assertThat(panListaAsociadosDTO1).isNotEqualTo(panListaAsociadosDTO2);
        panListaAsociadosDTO1.setId(null);
        assertThat(panListaAsociadosDTO1).isNotEqualTo(panListaAsociadosDTO2);
    }
}
