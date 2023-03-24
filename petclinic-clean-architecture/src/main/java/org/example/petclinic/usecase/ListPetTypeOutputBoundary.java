package org.example.petclinic.usecase;

import java.util.List;
import org.example.petclinic.helper.Callback;
import org.example.petclinic.model.PetType;

public interface ListPetTypeOutputBoundary extends Callback<List<PetType>> {

}
