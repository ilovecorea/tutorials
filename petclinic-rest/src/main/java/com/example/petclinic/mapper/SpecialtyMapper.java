package com.example.petclinic.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.example.petclinic.model.Specialty;
import com.example.petclinic.rest.dto.SpecialtyDto;

/**
 * Map Specialty & SpecialtyDto using mapstruct
 */
@Mapper
public interface SpecialtyMapper {
    Specialty toSpecialty(SpecialtyDto specialtyDto);

    SpecialtyDto toSpecialtyDto(Specialty specialty);

    List<SpecialtyDto> toSpecialtyDtos(List<Specialty> specialties);

    List<Specialty> toSpecialtys(List<SpecialtyDto> specialties);

}
