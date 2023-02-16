package org.example.petclinic.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link org.example.petclinic.model.Vet}.
 * NOTE: This class has been automatically generated from the {@link org.example.petclinic.model.Vet} original class using Vert.x codegen.
 */
public class VetConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Vet obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "firstName":
          if (member.getValue() instanceof String) {
            obj.setFirstName((String)member.getValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof Number) {
            obj.setId(((Number)member.getValue()).intValue());
          }
          break;
        case "lastName":
          if (member.getValue() instanceof String) {
            obj.setLastName((String)member.getValue());
          }
          break;
        case "specialties":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<org.example.petclinic.model.Specialty> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new org.example.petclinic.model.Specialty((io.vertx.core.json.JsonObject)item));
            });
            obj.setSpecialties(list);
          }
          break;
      }
    }
  }

   static void toJson(Vet obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(Vet obj, java.util.Map<String, Object> json) {
    if (obj.getFirstName() != null) {
      json.put("firstName", obj.getFirstName());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getLastName() != null) {
      json.put("lastName", obj.getLastName());
    }
    if (obj.getSpecialties() != null) {
      JsonArray array = new JsonArray();
      obj.getSpecialties().forEach(item -> array.add(item.toJson()));
      json.put("specialties", array);
    }
  }
}
