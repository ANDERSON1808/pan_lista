package com.pe.pandero.asamblea.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Balotas.
 */
@Table("balotas")
public class Balotas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("numero")
    private Float numero;

    @Column("creado")
    private Instant creado;

    @Transient
    private Sorteos sorteos;

    @Column("sorteos_id")
    private Long sorteosId;

    @Transient
    private PanListaAsociados panListaAsociados;

    @Column("pan_lista_asociados_id")
    private Long panListaAsociadosId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Balotas id(Long id) {
        this.id = id;
        return this;
    }

    public Float getNumero() {
        return this.numero;
    }

    public Balotas numero(Float numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Float numero) {
        this.numero = numero;
    }

    public Instant getCreado() {
        return this.creado;
    }

    public Balotas creado(Instant creado) {
        this.creado = creado;
        return this;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public Sorteos getSorteos() {
        return this.sorteos;
    }

    public Balotas sorteos(Sorteos sorteos) {
        this.setSorteos(sorteos);
        this.sorteosId = sorteos != null ? sorteos.getId() : null;
        return this;
    }

    public void setSorteos(Sorteos sorteos) {
        this.sorteos = sorteos;
        this.sorteosId = sorteos != null ? sorteos.getId() : null;
    }

    public Long getSorteosId() {
        return this.sorteosId;
    }

    public void setSorteosId(Long sorteos) {
        this.sorteosId = sorteos;
    }

    public PanListaAsociados getPanListaAsociados() {
        return this.panListaAsociados;
    }

    public Balotas panListaAsociados(PanListaAsociados panListaAsociados) {
        this.setPanListaAsociados(panListaAsociados);
        this.panListaAsociadosId = panListaAsociados != null ? panListaAsociados.getId() : null;
        return this;
    }

    public void setPanListaAsociados(PanListaAsociados panListaAsociados) {
        this.panListaAsociados = panListaAsociados;
        this.panListaAsociadosId = panListaAsociados != null ? panListaAsociados.getId() : null;
    }

    public Long getPanListaAsociadosId() {
        return this.panListaAsociadosId;
    }

    public void setPanListaAsociadosId(Long panListaAsociados) {
        this.panListaAsociadosId = panListaAsociados;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Balotas)) {
            return false;
        }
        return id != null && id.equals(((Balotas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Balotas{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", creado='" + getCreado() + "'" +
            "}";
    }
}
