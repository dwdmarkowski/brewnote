package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.UserGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserGroup and its DTO UserGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserGroupMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "friendGroup.id", target = "friendGroupId")
    @Mapping(source = "friendGroup.name", target = "friendGroupName")
    UserGroupDTO userGroupToUserGroupDTO(UserGroup userGroup);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "friendGroupId", target = "friendGroup")
    UserGroup userGroupDTOToUserGroup(UserGroupDTO userGroupDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    default FriendGroup friendGroupFromId(Long id) {
        if (id == null) {
            return null;
        }
        FriendGroup friendGroup = new FriendGroup();
        friendGroup.setId(id);
        return friendGroup;
    }
}
