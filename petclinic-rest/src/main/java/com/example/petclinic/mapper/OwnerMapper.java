package com.example.petclinic.mapper;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import com.example.petclinic.model.Owner;
import com.example.petclinic.rest.dto.OwnerDto;
import com.example.petclinic.rest.dto.OwnerFieldsDto;

/**
 * Maps Owner & OwnerDto using Mapstruct
 */
@Mapper(uses = PetMapper.class)
public interface OwnerMapper {

    OwnerDto toOwnerDto(Owner owner);

    Owner toOwner(OwnerDto ownerDto);

    Owner toOwner(OwnerFieldsDto ownerDto);

    List<OwnerDto> toOwnerDtoCollection(List<Owner> ownerCollection);

    List<Owner> toOwners(List<OwnerDto> ownerDtos);
}
