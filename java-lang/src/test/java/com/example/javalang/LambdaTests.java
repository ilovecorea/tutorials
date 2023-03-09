package com.example.javalang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

public class LambdaTests {

  public static String add(String s, Function<String, String> fn) {
    return fn.apply(s);
  }

  @Test
  void test1() {
    Function<String, String> fn = p -> p + " from lambda";
    String result = add("Message", fn);
    assertEquals("Message from lambda", result);
  }

  @Test
  void test2() {
    Map<String, Integer> nameMap = new HashMap<>();
    Integer value = nameMap.computeIfAbsent("John", String::length);
    System.out.println("value:" + value);

    assertEquals(new Integer(4), nameMap.get("John"));
    assertEquals(new Integer(4), value);
  }
}
