package com.pe.pandero.asamblea.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pe.pandero.asamblea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BalotasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Balotas.class);
        Balotas balotas1 = new Balotas();
        balotas1.setId(1L);
        Balotas balotas2 = new Balotas();
        balotas2.setId(balotas1.getId());
        assertThat(balotas1).isEqualTo(balotas2);
        balotas2.setId(2L);
        assertThat(balotas1).isNotEqualTo(balotas2);
        balotas1.setId(null);
        assertThat(balotas1).isNotEqualTo(balotas2);
    }
}
