package org.example.petclinic.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.util.List;
import lombok.Data;

@Data
@DataObject(generateConverter = true, publicConverter = false)
public class Vet {

  private Integer id;

  private String firstName;

  private String lastName;

  private List<Specialty> specialties;

  public Vet(JsonObject json) {
    VetConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    VetConverter.toJson(this, json);
    return json;
  }
}
