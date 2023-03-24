package org.example.petclinic.usecases.pettype;

import java.util.List;
import org.example.petclinic.helper.Callback;
import org.example.petclinic.entities.PetType;

public interface ListPetTypeOutputBoundary extends Callback<List<PetType>> {

}
