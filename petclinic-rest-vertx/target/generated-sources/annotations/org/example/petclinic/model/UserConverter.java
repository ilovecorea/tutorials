package org.example.petclinic.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link org.example.petclinic.model.User}.
 * NOTE: This class has been automatically generated from the {@link org.example.petclinic.model.User} original class using Vert.x codegen.
 */
public class UserConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, User obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "enabled":
          if (member.getValue() instanceof Boolean) {
            obj.setEnabled((Boolean)member.getValue());
          }
          break;
        case "password":
          if (member.getValue() instanceof String) {
            obj.setPassword((String)member.getValue());
          }
          break;
        case "roles":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<org.example.petclinic.model.Role> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new org.example.petclinic.model.Role((io.vertx.core.json.JsonObject)item));
            });
            obj.setRoles(list);
          }
          break;
        case "username":
          if (member.getValue() instanceof String) {
            obj.setUsername((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(User obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(User obj, java.util.Map<String, Object> json) {
    if (obj.getEnabled() != null) {
      json.put("enabled", obj.getEnabled());
    }
    if (obj.getPassword() != null) {
      json.put("password", obj.getPassword());
    }
    if (obj.getRoles() != null) {
      JsonArray array = new JsonArray();
      obj.getRoles().forEach(item -> array.add(item.toJson()));
      json.put("roles", array);
    }
    if (obj.getUsername() != null) {
      json.put("username", obj.getUsername());
    }
  }
}
