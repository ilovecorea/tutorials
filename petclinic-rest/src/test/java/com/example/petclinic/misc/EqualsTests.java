package com.example.petclinic.misc;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.petclinic.model.PetType;
import com.example.petclinic.service.BaseServiceTests;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EqualsTests extends BaseServiceTests {

  @Autowired
  EntityManagerFactory emf;

  @Test
  void test1() {
    EntityManager em = emf.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
    transaction.begin();
    PetType pet1 = new PetType();
    pet1.setName("pet1");
    em.persist(pet1);

    PetType pet2 = em.find(PetType.class, pet1.getId());
    //캐시가 초기화 되지 않아 두 객체는 동일하다.
    assertThat(pet1).isEqualTo(pet2);
    em.clear();
  }

  @Test
  void test2() {
    EntityManager em = emf.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
    transaction.begin();
    PetType pet1 = new PetType();
    pet1.setName("pet1");
    em.persist(pet1);
    em.clear();

    PetType pet2 = em.find(PetType.class, pet1.getId());
    //캐시가 초기화되어 두 객체는 서로 다르다.
//    assertThat(pet1).isNotEqualTo(pet2);
    //hashCode와 equals를 올바르게 구현하면 같은 객체로 판단.
    assertThat(pet1).isEqualTo(pet2);
  }
}