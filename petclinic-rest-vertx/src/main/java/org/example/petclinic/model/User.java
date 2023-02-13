package org.example.petclinic.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.util.List;
import lombok.Data;

@Data
@DataObject(generateConverter = true, publicConverter = false)
public class User {

  private String username;

  private String password;

  private Boolean enabled;

  private List<Role> roles;

  public User(JsonObject json) {
    UserConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    UserConverter.toJson(this, json);
    return json;
  }
}
