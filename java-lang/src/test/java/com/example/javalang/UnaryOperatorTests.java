package com.example.javalang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Test;

public class UnaryOperatorTests {

  @Test
  void test1() {
    Function<Integer, Integer> func = x -> x * 2;
    Integer result = func.apply(2);
    assertEquals(result, 4);

    UnaryOperator<Integer> func2 = x -> x * 2;
    Integer result2 = func2.apply(2);
    assertEquals(result2, 4);
  }

  public static <T> List<T> math(List<T> list, UnaryOperator<T> uo) {
    List<T> result = new ArrayList<>();
    for (T t : list) {
      result.add(uo.apply(t));
    }
    return result;
  }

  @Test
  void test2() {
    List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    List<Integer> result = math(list, x -> x * 2);
    System.out.println(result);
  }

  public static <T> List<T> math(List<T> list, UnaryOperator<T> uo, UnaryOperator<T> uo2) {
    List<T> result = new ArrayList<>();
    for (T t : list) {
      result.add(uo.andThen(uo2).apply(t));
    }
    return result;
  }

  @Test
  void test3() {
    List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    List<Integer> result = math(list, x -> x * 2, x -> x + 1);
    System.out.println(result);
  }
}
