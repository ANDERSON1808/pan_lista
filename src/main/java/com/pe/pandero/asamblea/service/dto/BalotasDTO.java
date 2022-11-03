package com.pe.pandero.asamblea.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.pe.pandero.asamblea.domain.Balotas} entity.
 */
public class BalotasDTO implements Serializable {

    private Long id;

    private Float numero;

    private Instant creado;

    private SorteosDTO sorteos;

    private PanListaAsociadosDTO panListaAsociados;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getNumero() {
        return numero;
    }

    public void setNumero(Float numero) {
        this.numero = numero;
    }

    public Instant getCreado() {
        return creado;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public SorteosDTO getSorteos() {
        return sorteos;
    }

    public void setSorteos(SorteosDTO sorteos) {
        this.sorteos = sorteos;
    }

    public PanListaAsociadosDTO getPanListaAsociados() {
        return panListaAsociados;
    }

    public void setPanListaAsociados(PanListaAsociadosDTO panListaAsociados) {
        this.panListaAsociados = panListaAsociados;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BalotasDTO)) {
            return false;
        }

        BalotasDTO balotasDTO = (BalotasDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, balotasDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BalotasDTO{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", creado='" + getCreado() + "'" +
            ", sorteos=" + getSorteos() +
            ", panListaAsociados=" + getPanListaAsociados() +
            "}";
    }
}
