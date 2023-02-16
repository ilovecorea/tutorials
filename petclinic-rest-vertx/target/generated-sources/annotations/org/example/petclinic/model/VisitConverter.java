package org.example.petclinic.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link org.example.petclinic.model.Visit}.
 * NOTE: This class has been automatically generated from the {@link org.example.petclinic.model.Visit} original class using Vert.x codegen.
 */
public class VisitConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Visit obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "description":
          if (member.getValue() instanceof String) {
            obj.setDescription((String)member.getValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof Number) {
            obj.setId(((Number)member.getValue()).intValue());
          }
          break;
        case "pet":
          if (member.getValue() instanceof JsonObject) {
            obj.setPet(new org.example.petclinic.model.Pet((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "petId":
          if (member.getValue() instanceof Number) {
            obj.setPetId(((Number)member.getValue()).intValue());
          }
          break;
      }
    }
  }

   static void toJson(Visit obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(Visit obj, java.util.Map<String, Object> json) {
    if (obj.getDescription() != null) {
      json.put("description", obj.getDescription());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getPet() != null) {
      json.put("pet", obj.getPet().toJson());
    }
    if (obj.getPetId() != null) {
      json.put("petId", obj.getPetId());
    }
  }
}
