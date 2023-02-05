package com.example.petclinic.mapper;

import com.example.petclinic.model.Owner;
import com.example.petclinic.rest.dto.OwnerDto;
import org.mapstruct.Mapper;

/**
 * Maps Owner & OwnerDto using Mapstruct
 */
@Mapper
public interface OwnerMapper extends BaseMapper<Owner, OwnerDto> {

}
