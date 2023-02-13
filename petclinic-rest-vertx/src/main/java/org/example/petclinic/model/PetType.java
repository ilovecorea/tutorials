package org.example.petclinic.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;

@Data
@DataObject(generateConverter = true, publicConverter = false)
public class PetType {

  protected Integer id;

  private String name;

  public PetType(JsonObject json) {
    PetTypeConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    PetTypeConverter.toJson(this, json);
    return json;
  }
}
