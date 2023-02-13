package org.example.petclinic.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.time.LocalDate;
import lombok.Data;
import lombok.ToString;

@Data
@DataObject(generateConverter = true, publicConverter = false)
public class Visit {

  private Integer id;

  private Integer petId;

  private LocalDate date;

  private String description;

  @ToString.Exclude
  private Pet pet;

  public Visit(JsonObject json) {
    VisitConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    VisitConverter.toJson(this, json);
    return json;
  }
}
