package com.pe.pandero.asamblea.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pe.pandero.asamblea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BalotasDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BalotasDTO.class);
        BalotasDTO balotasDTO1 = new BalotasDTO();
        balotasDTO1.setId(1L);
        BalotasDTO balotasDTO2 = new BalotasDTO();
        assertThat(balotasDTO1).isNotEqualTo(balotasDTO2);
        balotasDTO2.setId(balotasDTO1.getId());
        assertThat(balotasDTO1).isEqualTo(balotasDTO2);
        balotasDTO2.setId(2L);
        assertThat(balotasDTO1).isNotEqualTo(balotasDTO2);
        balotasDTO1.setId(null);
        assertThat(balotasDTO1).isNotEqualTo(balotasDTO2);
    }
}
