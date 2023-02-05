package com.example.petclinic.mapper;

import com.example.petclinic.model.Visit;
import com.example.petclinic.rest.dto.VisitDto;
import org.mapstruct.Mapper;

/**
 * Map Visit & VisitDto using mapstruct
 */
@Mapper(uses = PetMapper.class)
public interface VisitMapper extends BaseMapper<Visit, VisitDto> {

}
