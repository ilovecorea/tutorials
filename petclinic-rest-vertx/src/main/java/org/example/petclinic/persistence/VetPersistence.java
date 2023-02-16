package org.example.petclinic.persistence;

import io.vertx.core.Future;
import java.util.List;
import org.example.petclinic.model.Vet;

public interface VetPersistence {

  Future<Vet> findVetById(Integer id);

  Future<List<Vet>> findAllVets();

  Future<Integer> createVet(Vet vet);

  Future<Integer> updateVet(Vet vet);

  Future<Integer> deleteVet(Vet vet);

}
