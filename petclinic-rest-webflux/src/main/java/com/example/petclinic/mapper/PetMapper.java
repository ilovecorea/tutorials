package com.example.petclinic.mapper;

import com.example.petclinic.model.Pet;
import com.example.petclinic.rest.dto.PetDto;
import org.mapstruct.Mapper;

/**
 * Map Pet & PetDto using mapstruct
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet, PetDto> {

}
