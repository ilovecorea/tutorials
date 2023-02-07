package com.example.petclinic.mapper;

import com.example.petclinic.model.Role;
import com.example.petclinic.model.User;
import com.example.petclinic.rest.dto.RoleDto;
import com.example.petclinic.rest.dto.UserDto;
import java.util.Collection;
import org.mapstruct.Mapper;

/**
 * Map User/Role & UserDto/RoleDto using mapstruct
 */
@Mapper
public interface UserMapper {

  Role toRole(RoleDto roleDto);

  RoleDto toRoleDto(Role role);

  Collection<RoleDto> toRoleDtos(Collection<Role> roles);

  User toUser(UserDto userDto);

  UserDto toUserDto(User user);

  Collection<Role> toRoles(Collection<RoleDto> roleDtos);
}
