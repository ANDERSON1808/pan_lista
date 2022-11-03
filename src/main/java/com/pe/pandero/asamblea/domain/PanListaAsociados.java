package com.pe.pandero.asamblea.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PanListaAsociados.
 */
@Table("pan_lista_asociados")
public class PanListaAsociados implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("lista_asociados_id")
    private Integer listaAsociadosId;

    @Column("grupo")
    private Double grupo;

    @Column("posicion_id")
    private Double posicionId;

    @Size(max = 255)
    @Column("nombre_completo")
    private String nombreCompleto;

    @Size(max = 255)
    @Column("estado_habil")
    private String estadoHabil;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PanListaAsociados id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getListaAsociadosId() {
        return this.listaAsociadosId;
    }

    public PanListaAsociados listaAsociadosId(Integer listaAsociadosId) {
        this.listaAsociadosId = listaAsociadosId;
        return this;
    }

    public void setListaAsociadosId(Integer listaAsociadosId) {
        this.listaAsociadosId = listaAsociadosId;
    }

    public Double getGrupo() {
        return this.grupo;
    }

    public PanListaAsociados grupo(Double grupo) {
        this.grupo = grupo;
        return this;
    }

    public void setGrupo(Double grupo) {
        this.grupo = grupo;
    }

    public Double getPosicionId() {
        return this.posicionId;
    }

    public PanListaAsociados posicionId(Double posicionId) {
        this.posicionId = posicionId;
        return this;
    }

    public void setPosicionId(Double posicionId) {
        this.posicionId = posicionId;
    }

    public String getNombreCompleto() {
        return this.nombreCompleto;
    }

    public PanListaAsociados nombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        return this;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEstadoHabil() {
        return this.estadoHabil;
    }

    public PanListaAsociados estadoHabil(String estadoHabil) {
        this.estadoHabil = estadoHabil;
        return this;
    }

    public void setEstadoHabil(String estadoHabil) {
        this.estadoHabil = estadoHabil;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PanListaAsociados)) {
            return false;
        }
        return id != null && id.equals(((PanListaAsociados) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PanListaAsociados{" +
            "id=" + getId() +
            ", listaAsociadosId=" + getListaAsociadosId() +
            ", grupo=" + getGrupo() +
            ", posicionId=" + getPosicionId() +
            ", nombreCompleto='" + getNombreCompleto() + "'" +
            ", estadoHabil='" + getEstadoHabil() + "'" +
            "}";
    }
}
