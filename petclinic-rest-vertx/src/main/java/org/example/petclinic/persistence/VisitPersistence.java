package org.example.petclinic.persistence;

import io.vertx.core.Future;
import java.util.List;
import org.example.petclinic.model.Visit;

public interface VisitPersistence {

  Future<List<Visit>> findVisitsByPetId(Integer petId);

  Future<Visit> findVisitById(Integer visitId);

  Future<List<Visit>> findAllVisits();

  Future<Integer> createVisit(Visit visit);

  Future<Integer> updateVisit(Visit visit);

  Future<Integer> deleteVisit(Visit visit);

}
