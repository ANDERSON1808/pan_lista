package com.pe.pandero.asamblea.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.pe.pandero.asamblea.domain.Sorteos} entity.
 */
public class SorteosDTO implements Serializable {

    private Long id;

    private Instant fechaSorteo;

    private String codigo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaSorteo() {
        return fechaSorteo;
    }

    public void setFechaSorteo(Instant fechaSorteo) {
        this.fechaSorteo = fechaSorteo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SorteosDTO)) {
            return false;
        }

        SorteosDTO sorteosDTO = (SorteosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sorteosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SorteosDTO{" +
            "id=" + getId() +
            ", fechaSorteo='" + getFechaSorteo() + "'" +
            ", codigo='" + getCodigo() + "'" +
            "}";
    }
}
