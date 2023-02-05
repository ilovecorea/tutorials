package com.example.petclinic.mapper;

import com.example.petclinic.model.Specialty;
import com.example.petclinic.rest.dto.SpecialtyDto;
import org.mapstruct.Mapper;

/**
 * Map Specialty & SpecialtyDto using mapstruct
 */
@Mapper
public interface SpecialtyMapper extends BaseMapper<Specialty, SpecialtyDto> {

}
