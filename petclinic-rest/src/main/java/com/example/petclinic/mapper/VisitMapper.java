package com.example.petclinic.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.petclinic.model.Visit;
import com.example.petclinic.rest.dto.VisitDto;
import com.example.petclinic.rest.dto.VisitFieldsDto;

/**
 * Map Visit & VisitDto using mapstruct
 */
@Mapper(uses = PetMapper.class)
public interface VisitMapper {
    Visit toVisit(VisitDto visitDto);

    Visit toVisit(VisitFieldsDto visitFieldsDto);

    @Mapping(source = "pet.id", target = "petId")
    VisitDto toVisitDto(Visit visit);

    List<VisitDto> toVisitsDto(List<Visit> visits);

}
