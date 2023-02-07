package com.example.petclinic.mapper;

import com.example.petclinic.model.Specialty;
import com.example.petclinic.rest.dto.SpecialtyDto;
import java.util.Collection;
import org.mapstruct.Mapper;

/**
 * Map Specialty & SpecialtyDto using mapstruct
 */
@Mapper
public interface SpecialtyMapper {

  Specialty toSpecialty(SpecialtyDto specialtyDto);

  SpecialtyDto toSpecialtyDto(Specialty specialty);

  Collection<SpecialtyDto> toSpecialtyDtos(Collection<Specialty> specialties);

  Collection<Specialty> toSpecialtys(Collection<SpecialtyDto> specialties);
}
