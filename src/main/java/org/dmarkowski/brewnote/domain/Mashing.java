package org.dmarkowski.brewnote.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Mashing.
 */
@Entity
@Table(name = "mashing")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mashing implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "time")
    private Integer time;

    @Column(name = "temperature")
    private Integer temperature;

    @ManyToOne
    @JoinColumn(name = "malt_id")
    private Malt malt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Malt getMalt() {
        return malt;
    }

    public void setMalt(Malt malt) {
        this.malt = malt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mashing mashing = (Mashing) o;
        return Objects.equals(id, mashing.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Mashing{" +
            "id=" + id +
            ", time='" + time + "'" +
            ", temperature='" + temperature + "'" +
            '}';
    }
}
