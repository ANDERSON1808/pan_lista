package com.pe.pandero.asamblea.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pe.pandero.asamblea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SorteosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sorteos.class);
        Sorteos sorteos1 = new Sorteos();
        sorteos1.setId(1L);
        Sorteos sorteos2 = new Sorteos();
        sorteos2.setId(sorteos1.getId());
        assertThat(sorteos1).isEqualTo(sorteos2);
        sorteos2.setId(2L);
        assertThat(sorteos1).isNotEqualTo(sorteos2);
        sorteos1.setId(null);
        assertThat(sorteos1).isNotEqualTo(sorteos2);
    }
}
