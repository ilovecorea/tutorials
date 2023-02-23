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
public class Role {

  protected Integer id;
  private String username;
  private String name;

  public Role(String username, String name) {
    this.username = username;
    this.name = name;
  }

  public Role(JsonObject json) {
    RoleConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    RoleConverter.toJson(this, json);
    return json;
  }
}
