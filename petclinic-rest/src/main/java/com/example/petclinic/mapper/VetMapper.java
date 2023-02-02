package com.example.petclinic.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.example.petclinic.model.Vet;
import com.example.petclinic.rest.dto.VetDto;
import com.example.petclinic.rest.dto.VetFieldsDto;

/**
 * Map Vet & VetoDto using mapstruct
 */
@Mapper(uses = SpecialtyMapper.class)
public interface VetMapper {
    Vet toVet(VetDto vetDto);

    Vet toVet(VetFieldsDto vetFieldsDto);

    VetDto toVetDto(Vet vet);

    List<VetDto> toVetDtos(List<Vet> vets);
}
