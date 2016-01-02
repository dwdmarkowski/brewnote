package org.dmarkowski.brewnote.web.rest.mapper;

import org.dmarkowski.brewnote.domain.*;
import org.dmarkowski.brewnote.web.rest.dto.FriendGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FriendGroup and its DTO FriendGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FriendGroupMapper {

    FriendGroupDTO friendGroupToFriendGroupDTO(FriendGroup friendGroup);

    @Mapping(target = "userGroups", ignore = true)
    @Mapping(target = "groupSharedRecipes", ignore = true)
    FriendGroup friendGroupDTOToFriendGroup(FriendGroupDTO friendGroupDTO);
}
