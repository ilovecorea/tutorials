package com.example.javalang;

import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

public class ConsumerTests {

  @Test
  public void test1() {
    Consumer<String> print = x -> System.out.println(x);
    print.accept("java");
  }

  static <T> void forEach(List<T> list, Consumer<T> consumer) {
    for (T t : list) {
      consumer.accept(t);
    }
  }

  @Test
  public void test2() {
    List<Integer> list = List.of(1,2,3);
    Consumer<Integer> consumer = x -> System.out.println(x);
    forEach(list, consumer);
    forEach(list, System.out::println);
  }
}
