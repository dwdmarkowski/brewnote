package org.dmarkowski.brewnote.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Hop entity.
 */
public class HopDTO implements Serializable {

    private Long id;

    private String name;

    private Double alfaAcids;

    private String hopForm;

    private Long recipeId;

    private String recipeName;

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

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HopDTO hopDTO = (HopDTO) o;

        if ( ! Objects.equals(id, hopDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HopDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", alfaAcids='" + alfaAcids + "'" +
            ", hopForm='" + hopForm + "'" +
            '}';
    }
}
