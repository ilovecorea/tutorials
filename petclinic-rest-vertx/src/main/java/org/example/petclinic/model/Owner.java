package org.example.petclinic.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.util.List;
import lombok.Data;

@Data
@DataObject(generateConverter = true, publicConverter = false)
public class Owner {

  private Integer id;

  private String firstName;

  private String lastName;

  private String address;

  private String city;

  private String telephone;

  private List<Pet> pets;

  public Owner(JsonObject json) {
    OwnerConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    OwnerConverter.toJson(this, json);
    return json;
  }
}
