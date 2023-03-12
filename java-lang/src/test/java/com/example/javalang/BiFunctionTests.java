package com.example.javalang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.function.BiFunction;
import org.junit.jupiter.api.Test;

public class BiFunctionTests {

  @Test
  void test1() {
    BiFunction<Integer, Integer, Integer> func = (x1, x2) -> x1 + x2;
    Integer result = func.apply(2, 3);
    assertEquals(result, 5);

    BiFunction<Integer, Integer, Double> func2 = (x1, x2) -> Math.pow(x1, x2);
    Double result2 = func2.apply(2, 4);
    assertEquals(result2, 16.0);

    BiFunction<Integer, Integer, List<Integer>> func3 = (x1, x2) -> List.of(x1 + x2);
    List<Integer> result3 = func3.apply(2, 3);
    assertEquals(result3, List.of(5));
  }
}
