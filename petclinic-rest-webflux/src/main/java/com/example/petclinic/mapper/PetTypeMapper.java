package com.example.petclinic.mapper;

import com.example.petclinic.model.PetType;
import com.example.petclinic.rest.dto.PetTypeDto;
import org.mapstruct.Mapper;

/**
 * Map PetType & PetTypeDto using mapstruct
 */
@Mapper
public interface PetTypeMapper extends BaseMapper<PetType, PetTypeDto> {

}
