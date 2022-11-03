package com.pe.pandero.asamblea.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pe.pandero.asamblea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SorteosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SorteosDTO.class);
        SorteosDTO sorteosDTO1 = new SorteosDTO();
        sorteosDTO1.setId(1L);
        SorteosDTO sorteosDTO2 = new SorteosDTO();
        assertThat(sorteosDTO1).isNotEqualTo(sorteosDTO2);
        sorteosDTO2.setId(sorteosDTO1.getId());
        assertThat(sorteosDTO1).isEqualTo(sorteosDTO2);
        sorteosDTO2.setId(2L);
        assertThat(sorteosDTO1).isNotEqualTo(sorteosDTO2);
        sorteosDTO1.setId(null);
        assertThat(sorteosDTO1).isNotEqualTo(sorteosDTO2);
    }
}
