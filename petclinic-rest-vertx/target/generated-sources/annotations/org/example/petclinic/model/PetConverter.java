package org.example.petclinic.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link org.example.petclinic.model.Pet}.
 * NOTE: This class has been automatically generated from the {@link org.example.petclinic.model.Pet} original class using Vert.x codegen.
 */
public class PetConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Pet obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "birthDate":
          if (member.getValue() instanceof String) {
            obj.setBirthDate((String)member.getValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof Number) {
            obj.setId(((Number)member.getValue()).intValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "owner":
          if (member.getValue() instanceof JsonObject) {
            obj.setOwner(new org.example.petclinic.model.Owner((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "ownerId":
          if (member.getValue() instanceof Number) {
            obj.setOwnerId(((Number)member.getValue()).intValue());
          }
          break;
        case "type":
          if (member.getValue() instanceof JsonObject) {
            obj.setType(new org.example.petclinic.model.PetType((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "typeId":
          if (member.getValue() instanceof Number) {
            obj.setTypeId(((Number)member.getValue()).intValue());
          }
          break;
        case "visits":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<org.example.petclinic.model.Visit> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new org.example.petclinic.model.Visit((io.vertx.core.json.JsonObject)item));
            });
            obj.setVisits(list);
          }
          break;
      }
    }
  }

   static void toJson(Pet obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(Pet obj, java.util.Map<String, Object> json) {
    if (obj.getBirthDate() != null) {
      json.put("birthDate", obj.getBirthDate());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getOwner() != null) {
      json.put("owner", obj.getOwner().toJson());
    }
    if (obj.getOwnerId() != null) {
      json.put("ownerId", obj.getOwnerId());
    }
    if (obj.getType() != null) {
      json.put("type", obj.getType().toJson());
    }
    if (obj.getTypeId() != null) {
      json.put("typeId", obj.getTypeId());
    }
    if (obj.getVisits() != null) {
      JsonArray array = new JsonArray();
      obj.getVisits().forEach(item -> array.add(item.toJson()));
      json.put("visits", array);
    }
  }
}
