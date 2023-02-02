package com.example.petclinic.mapper;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import com.example.petclinic.model.PetType;
import com.example.petclinic.rest.dto.PetTypeDto;
import com.example.petclinic.rest.dto.PetTypeFieldsDto;

/**
 * Map PetType & PetTypeDto using mapstruct
 */
@Mapper
public interface PetTypeMapper {

    PetType toPetType(PetTypeDto petTypeDto);

    PetType toPetType(PetTypeFieldsDto petTypeFieldsDto);

    PetTypeDto toPetTypeDto(PetType petType);

    List<PetTypeDto> toPetTypeDtos(List<PetType> petTypes);
}
