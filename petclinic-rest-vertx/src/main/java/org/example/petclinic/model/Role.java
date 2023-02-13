package org.example.petclinic.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.ToString;

@Data
@DataObject(generateConverter = true, publicConverter = false)
public class Role {

  protected Integer id;

  private String name;

  @ToString.Exclude
  private User user;

  public Role(JsonObject json) {
    RoleConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    RoleConverter.toJson(this, json);
    return json;
  }
}
