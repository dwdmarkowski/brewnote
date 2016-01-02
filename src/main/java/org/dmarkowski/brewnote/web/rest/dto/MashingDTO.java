package org.dmarkowski.brewnote.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Mashing entity.
 */
public class MashingDTO implements Serializable {

    private Long id;

    private Integer time;

    private Integer temperature;

    private Long maltId;

    private String maltName;

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

    public Long getMaltId() {
        return maltId;
    }

    public void setMaltId(Long maltId) {
        this.maltId = maltId;
    }

    public String getMaltName() {
        return maltName;
    }

    public void setMaltName(String maltName) {
        this.maltName = maltName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MashingDTO mashingDTO = (MashingDTO) o;

        if ( ! Objects.equals(id, mashingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MashingDTO{" +
            "id=" + id +
            ", time='" + time + "'" +
            ", temperature='" + temperature + "'" +
            '}';
    }
}
