package com.example.petclinic.mapper;

import com.example.petclinic.model.Vet;
import com.example.petclinic.rest.dto.VetDto;
import org.mapstruct.Mapper;

/**
 * Map Vet & VetoDto using mapstruct
 */
@Mapper
public interface VetMapper extends BaseMapper<Vet, VetDto> {
}
