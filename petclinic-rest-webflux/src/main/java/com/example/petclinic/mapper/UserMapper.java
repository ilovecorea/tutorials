package com.example.petclinic.mapper;

import com.example.petclinic.model.Role;
import com.example.petclinic.rest.dto.RoleDto;
import org.mapstruct.Mapper;

/**
 * Map User/Role & UserDto/RoleDto using mapstruct
 */
@Mapper
public interface UserMapper extends BaseMapper<Role, RoleDto> {

}
