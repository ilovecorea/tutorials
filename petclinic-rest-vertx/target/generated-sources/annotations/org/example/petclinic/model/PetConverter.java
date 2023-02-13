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
      }
    }
  }

   static void toJson(Pet obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(Pet obj, java.util.Map<String, Object> json) {
  }
}
