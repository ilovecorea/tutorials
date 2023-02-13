package org.example.petclinic.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@DataObject(generateConverter = true, publicConverter = false)
public class Pet {

  private Integer id;

  private String name;

  private LocalDate birthDate;

  private Integer typeId;

  private Integer ownerId;

  private PetType type;

  @ToString.Exclude
  private Owner owner;

  private List<Visit> visits;

  public Pet(JsonObject json) {
    PetConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    PetConverter.toJson(this, json);
    return json;
  }
}
