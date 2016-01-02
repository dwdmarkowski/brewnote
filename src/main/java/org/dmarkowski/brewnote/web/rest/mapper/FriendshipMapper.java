package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.FriendshipDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Friendship and its DTO FriendshipDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FriendshipMapper {

    @Mapping(source = "firstUser.id", target = "firstUserId")
    @Mapping(source = "firstUser.login", target = "firstUserLogin")
    @Mapping(source = "secondUser.id", target = "secondUserId")
    @Mapping(source = "secondUser.login", target = "secondUserLogin")
    FriendshipDTO friendshipToFriendshipDTO(Friendship friendship);

    @Mapping(source = "firstUserId", target = "firstUser")
    @Mapping(source = "secondUserId", target = "secondUser")
    Friendship friendshipDTOToFriendship(FriendshipDTO friendshipDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
