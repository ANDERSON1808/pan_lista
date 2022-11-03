package com.pe.pandero.asamblea.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Sorteos.
 */
@Table("sorteos")
public class Sorteos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("fecha_sorteo")
    private Instant fechaSorteo;

    @Column("codigo")
    private String codigo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sorteos id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getFechaSorteo() {
        return this.fechaSorteo;
    }

    public Sorteos fechaSorteo(Instant fechaSorteo) {
        this.fechaSorteo = fechaSorteo;
        return this;
    }

    public void setFechaSorteo(Instant fechaSorteo) {
        this.fechaSorteo = fechaSorteo;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Sorteos codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sorteos)) {
            return false;
        }
        return id != null && id.equals(((Sorteos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sorteos{" +
            "id=" + getId() +
            ", fechaSorteo='" + getFechaSorteo() + "'" +
            ", codigo='" + getCodigo() + "'" +
            "}";
    }
}
