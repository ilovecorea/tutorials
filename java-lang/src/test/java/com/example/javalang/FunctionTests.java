package com.example.javalang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.JApplet;
import org.junit.jupiter.api.Test;

public class FunctionTests {

  @Test
  public void test1() {
    Function<String, Integer> func = x -> x.length();
    Integer apply = func.apply("ricky");
    assertEquals(apply, 5);
  }

  @Test
  public void test2() {
    Function<String, Integer> func = x -> x.length();
    Function<Integer, Integer> func2 = x -> x * 2;
    Integer result = func.andThen(func2).apply("ricky");
    assertEquals(result, 10);
  }

  public <T, R> Map<T, R> convertListToMap(List<T> list, Function<T, R> func) {
    Map<T, R> result = new HashMap<>();
    for (T t : list) {
      result.put(t, func.apply(t));
    }
    return result;
  }

  @Test
  public void test3() {
    List<String> list = Arrays.asList("node", "c++", "java", "javascript");
    Map<String, Integer> map = convertListToMap(list, x -> x.length());
    Map<String, Integer> map1 = convertListToMap(list, String::length);
    assertEquals(map, map1);
  }
}
