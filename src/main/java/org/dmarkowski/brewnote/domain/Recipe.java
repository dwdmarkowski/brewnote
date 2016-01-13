package org.dmarkowski.brewnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Recipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "style")
    private String style;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "abv")
    private Double abv;

    @Column(name = "original_gravity")
    private Double originalGravity;

    @Column(name = "notes")
    private String notes;

    @Column(name = "volume")
    private Float volume;

    @Column(name = "visibility")
    private String visibility;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GroupSharedRecipe> groupSharedRecipes = new HashSet<>();

    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Malt> malts = new HashSet<>();

    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Hop> hops = new HashSet<>();

    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Yeast> yeasts = new HashSet<>();

    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Additional> additionals = new HashSet<>();

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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Double getAbv() {
        return abv;
    }

    public void setAbv(Double abv) {
        this.abv = abv;
    }

    public Double getOriginalGravity() {
        return originalGravity;
    }

    public void setOriginalGravity(Double originalGravity) {
        this.originalGravity = originalGravity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<GroupSharedRecipe> getGroupSharedRecipes() {
        return groupSharedRecipes;
    }

    public void setGroupSharedRecipes(Set<GroupSharedRecipe> groupSharedRecipes) {
        this.groupSharedRecipes = groupSharedRecipes;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Malt> getMalts() {
        return malts;
    }

    public void setMalts(Set<Malt> malts) {
        this.malts = malts;
    }

    public Set<Hop> getHops() {
        return hops;
    }

    public void setHops(Set<Hop> hops) {
        this.hops = hops;
    }

    public Set<Yeast> getYeasts() {
        return yeasts;
    }

    public void setYeasts(Set<Yeast> yeasts) {
        this.yeasts = yeasts;
    }

    public Set<Additional> getAdditionals() {
        return additionals;
    }

    public void setAdditionals(Set<Additional> additionals) {
        this.additionals = additionals;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", style='" + style + "'" +
            ", date='" + date + "'" +
            ", abv='" + abv + "'" +
            ", originalGravity='" + originalGravity + "'" +
            ", notes='" + notes + "'" +
            ", volume='" + volume + "'" +
            '}';
    }
}
