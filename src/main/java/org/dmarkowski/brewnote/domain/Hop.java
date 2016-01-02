package org.dmarkowski.brewnote.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Hop.
 */
@Entity
@Table(name = "hop")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hop implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "alfa_acids")
    private Double alfaAcids;

    @Column(name = "hop_form")
    private String hopForm;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAlfaAcids() {
        return alfaAcids;
    }

    public void setAlfaAcids(Double alfaAcids) {
        this.alfaAcids = alfaAcids;
    }

    public String getHopForm() {
        return hopForm;
    }

    public void setHopForm(String hopForm) {
        this.hopForm = hopForm;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hop hop = (Hop) o;
        return Objects.equals(id, hop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Hop{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", alfaAcids='" + alfaAcids + "'" +
            ", hopForm='" + hopForm + "'" +
            '}';
    }
}
