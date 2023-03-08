package com.example.petclinic.misc;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.petclinic.model.PetType;
import com.example.petclinic.repository.PetTypeRepository;
import com.example.petclinic.service.BaseServiceTests;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles({"local"})
public class LifeCycleEventTests extends BaseServiceTests {

  private static final Logger log = LoggerFactory.getLogger(LifeCycleEventTests.class);
  private static List<Integer> ids = new ArrayList<>();

  @Autowired
  private PetTypeRepository petTypeRepository;

  @Test
  @Order(1)
  void test1() {
    log.debug("============= 테스트1 ============");
    PetType petType = new PetType();
    petType.setId(100);
    petType.setName("강아지");
    petTypeRepository.save(petType);
    log.debug("저장된 id:{}", petType.getId());
    ids.add(petType.getId());
    petType.setName("푸들");
  }

  @Test
  @Order(2)
  void test2() {
    log.debug("============= 테스트2 ============");
    PetType petType = new PetType();
    petType.setId(101);
    petType.setName("강아지");
    petTypeRepository.save(petType);
    log.debug("저장된 id:{}", petType.getId());
    ids.add(petType.getId());
    petType.setName("말티즈");
    petTypeRepository.save(petType);
  }

  @Test
  @Order(3)
  void test3() {
    log.debug("============= 테스트3 ============");
    Optional<PetType> pet1 = petTypeRepository.findById(ids.get(0));
    assertThat(pet1.isPresent()).isTrue();
    assertThat(pet1.get().getName()).isEqualTo("강아지");

    Optional<PetType> pet2 = petTypeRepository.findById(ids.get(1));
    assertThat(pet2.isPresent()).isTrue();
    assertThat(pet2.get().getName()).isEqualTo("말티즈");
  }

  @Test
  @Order(4)
  void test4() {
    log.debug("============= 테스트4 ============");
    petTypeRepository.deleteById(ids.get(0));
    petTypeRepository.deleteById(ids.get(1));
    List<PetType> petTypes = petTypeRepository.findAll();
    assertThat(petTypes.size()).isEqualTo(6);
  }

  @Test
  @Transactional
  void test5() {
    log.debug("============= 테스트5 ============");
    PetType petType = new PetType();
    petType.setName("강아지");
    petTypeRepository.save(petType);
    log.debug("저장된 id:{}", petType.getId());
    ids.add(petType.getId());
    petType.setName("푸들");
    PetType pet1 = petTypeRepository.findById(petType.getId()).get();
    assertThat(pet1.getName()).isEqualTo("푸들");

  }

  @Test
  @Transactional
  void test6() {
    log.debug("============= 테스트6 ============");
    PetType petType = new PetType();
    petType.setName("돼지");
    petTypeRepository.save(petType);
    log.debug("저장된 id:{}", petType.getId());
    ids.add(petType.getId());
    petType.setName("꿀꿀이");

    //flush를 수행하면 update 쿼리가 수행된다.
    petTypeRepository.flush();

    PetType pet1 = petTypeRepository.findById(petType.getId()).get();
    assertThat(pet1.getName()).isEqualTo("꿀꿀이");

  }
}
