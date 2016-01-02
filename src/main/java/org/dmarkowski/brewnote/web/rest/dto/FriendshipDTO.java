package org.dmarkowski.brewnote.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Friendship entity.
 */
public class FriendshipDTO implements Serializable {

    private Long id;

    private String status;

    private Long firstUserId;

    private String firstUserLogin;

    private Long secondUserId;

    private String secondUserLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(Long userId) {
        this.firstUserId = userId;
    }

    public String getFirstUserLogin() {
        return firstUserLogin;
    }

    public void setFirstUserLogin(String userLogin) {
        this.firstUserLogin = userLogin;
    }

    public Long getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(Long userId) {
        this.secondUserId = userId;
    }

    public String getSecondUserLogin() {
        return secondUserLogin;
    }

    public void setSecondUserLogin(String userLogin) {
        this.secondUserLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FriendshipDTO friendshipDTO = (FriendshipDTO) o;

        if ( ! Objects.equals(id, friendshipDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FriendshipDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            '}';
    }
}
