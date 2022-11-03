package com.pe.pandero.asamblea.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pe.pandero.asamblea.domain.PanListaAsociados} entity.
 */
public class PanListaAsociadosDTO implements Serializable {

    private Long id;

    private Integer listaAsociadosId;

    private Double grupo;

    private Double posicionId;

    @Size(max = 255)
    private String nombreCompleto;

    @Size(max = 255)
    private String estadoHabil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getListaAsociadosId() {
        return listaAsociadosId;
    }

    public void setListaAsociadosId(Integer listaAsociadosId) {
        this.listaAsociadosId = listaAsociadosId;
    }

    public Double getGrupo() {
        return grupo;
    }

    public void setGrupo(Double grupo) {
        this.grupo = grupo;
    }

    public Double getPosicionId() {
        return posicionId;
    }

    public void setPosicionId(Double posicionId) {
        this.posicionId = posicionId;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEstadoHabil() {
        return estadoHabil;
    }

    public void setEstadoHabil(String estadoHabil) {
        this.estadoHabil = estadoHabil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PanListaAsociadosDTO)) {
            return false;
        }

        PanListaAsociadosDTO panListaAsociadosDTO = (PanListaAsociadosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, panListaAsociadosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PanListaAsociadosDTO{" +
            "id=" + getId() +
            ", listaAsociadosId=" + getListaAsociadosId() +
            ", grupo=" + getGrupo() +
            ", posicionId=" + getPosicionId() +
            ", nombreCompleto='" + getNombreCompleto() + "'" +
            ", estadoHabil='" + getEstadoHabil() + "'" +
            "}";
    }
}
