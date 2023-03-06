package com.example.petclinic.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import com.example.petclinic.rest.dto.OwnerDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OwnerMapperTests {

  @Autowired
  private OwnerMapper ownerMapper;

  private static final Logger log = LoggerFactory.getLogger(OwnerMapperTests.class);

  @Test
  void ownerEntityToOwnerDto() {
    Owner owner = new Owner();
    owner.setId(1);
    owner.setAddress("address");
    owner.setCity("city");
    owner.setTelephone("telephone");

    PetType petType = new PetType();
    petType.setId(1);
    petType.setName("cat");

    List<Pet> pets = new ArrayList<>();
    Pet pet = new Pet();
    pet.setId(1);
    pet.setName("pet name");
    pet.setType(petType);
    pet.setOwner(owner);
    pets.add(pet);

    owner.setPets(pets);
    OwnerDto ownerDto = ownerMapper.toOwnerDto(owner);
    //owner
    assertThat(ownerDto.getId()).isEqualTo(owner.getId());
    assertThat(ownerDto.getAddress()).isEqualTo(owner.getAddress());
    assertThat(ownerDto.getCity()).isEqualTo(owner.getCity());
    assertThat(ownerDto.getFirstName()).isNull();
    //pet
    assertThat(ownerDto.getPets().size()).isEqualTo(1);
    assertThat(ownerDto.getPets().get(0).getOwnerId()).isEqualTo(owner.getId());
    assertThat(ownerDto.getPets().get(0).getName()).isEqualTo(pet.getName());
    //petType
    assertThat(ownerDto.getPets().get(0).getType().getId()).isEqualTo(petType.getId());
    assertThat(ownerDto.getPets().get(0).getType().getName()).isEqualTo(petType.getName());
    //owner list
    List<Owner> ownerList = List.of(owner);
    List<OwnerDto> ownerDtoList = ownerMapper.toOwnerDtoCollection(ownerList);
    assertThat(ownerDtoList.size()).isEqualTo(1);
    assertThat(ownerDtoList.get(0).getPets().size()).isEqualTo(owner.getPets().size());

    log.debug("{}", ownerDtoList);
  }
}
