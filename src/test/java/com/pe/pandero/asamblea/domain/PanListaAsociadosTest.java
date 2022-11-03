package com.pe.pandero.asamblea.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pe.pandero.asamblea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PanListaAsociadosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PanListaAsociados.class);
        PanListaAsociados panListaAsociados1 = new PanListaAsociados();
        panListaAsociados1.setId(1L);
        PanListaAsociados panListaAsociados2 = new PanListaAsociados();
        panListaAsociados2.setId(panListaAsociados1.getId());
        assertThat(panListaAsociados1).isEqualTo(panListaAsociados2);
        panListaAsociados2.setId(2L);
        assertThat(panListaAsociados1).isNotEqualTo(panListaAsociados2);
        panListaAsociados1.setId(null);
        assertThat(panListaAsociados1).isNotEqualTo(panListaAsociados2);
    }
}
