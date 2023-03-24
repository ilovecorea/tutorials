package org.example.petclinic.entities;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;
import java.util.List;
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
@DataObject(generateConverter = true, publicConverter = false)
@RowMapped(formatter = SnakeCase.class)
@ParametersMapped(formatter = SnakeCase.class)
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
