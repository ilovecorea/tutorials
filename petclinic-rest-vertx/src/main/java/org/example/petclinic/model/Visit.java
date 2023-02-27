package org.example.petclinic.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RowMapped(formatter = SnakeCase.class)
@ParametersMapped(formatter = SnakeCase.class)
@DataObject(generateConverter = true, publicConverter = false)
public class Visit {

  private Integer id;

  private Integer petId;

  private String date;

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
