package org.example.petclinic.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;

@Data
@DataObject(generateConverter = true, publicConverter = false)
public class Specialty {

  protected Integer id;

  private String name;

  public Specialty(JsonObject json) {
    SpecialtyConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    SpecialtyConverter.toJson(this, json);
    return json;
  }
}
