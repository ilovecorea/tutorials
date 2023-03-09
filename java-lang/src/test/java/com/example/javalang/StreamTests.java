package com.example.javalang;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class StreamTests {

  @Test
  void test1() {
    //iterator
    List<String> list = List.of("a", "b", "c");
    Iterator<String> iterator = list.iterator();
    while (iterator.hasNext()) {
      String value = iterator.next();
      System.out.println(value);
    }

    //for each
    for (String value : list) {
      System.out.println(value);
    }

    //stream
    list.stream().forEach(System.out::println);
  }

  @Test
  void test2() {
    List<String> list = List.of("a", "b", "c");
    Stream<String> stream = list.stream();

    String[] array = list.toArray(new String[0]);
    Stream<String> stream1 = Arrays.stream(array);

    Stream<String> stream2 = Stream.<String>builder()
        .add("a")
        .add("b")
        .add("c")
        .build();

    stream.forEach(System.out::println);
    stream1.forEach(System.out::println);
    stream2.forEach(System.out::println);
  }

  @Test
  void test3() {
    Stream<String> stream = Stream.generate(() -> "Hello").limit(5);
    stream.forEach(System.out::println);
  }

  @Test
  void test4() {
    Stream<Integer> stream = Stream.iterate(100, n -> n + 10).limit(5);
    stream.forEach(System.out::println);
  }

  @Test
  void test5() {
    Stream<String> stream = Stream.empty();
    stream.forEach(System.out::println);
  }

  @Test
  void test6() {
    Stream<Integer> intStream = IntStream.range(1, 10).boxed();
    Stream<Long> longStream = LongStream.range(1, 10000).boxed();
    Stream<Double> doubleStream = new Random().doubles(3).boxed();

    doubleStream.forEach(System.out::println);
  }

  @Test
  void test7() {
    Stream<String> stream = Pattern.compile(",").splitAsStream("Apple,Banana,Melon");
    stream.forEach(System.out::println);
  }

  @Test
  void test8() {
    Stream<String> stream1 = Stream.of("Apple", "Banana", "Melon");
    Stream<String> stream2 = Stream.of("Kim", "Lee", "Park");
    Stream<String> stream3 = Stream.concat(stream1, stream2);
    stream3.forEach(System.out::println);
  }

  @Test
  void test9() {
    //Stream<T> filter(Predicate<? super T> predicate);
    Stream<Integer> stream = IntStream.range(1, 10).boxed();
    stream.filter(v -> ((v % 2) == 0))
        .forEach(System.out::println);
  }

  @Test
  void test10() {
    //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
    Stream<Integer> stream = IntStream.range(1, 10).boxed();
    stream.filter(v -> ((v % 2) == 0))
        .map(v -> v * 10)
        .forEach(System.out::println);
  }

  @Test
  void test11() {
    //<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
    List<List<String>> list = List.of(List.of("A", "B", "C"),
        Arrays.asList("a", "b", "c"));
    List<String> flatList = list.stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Test
  void test12() {
    int sum = IntStream.range(1, 10).sum();
    long count = IntStream.range(1, 10).count();

    OptionalInt max = IntStream.range(1, 10).max();
    OptionalInt min = IntStream.range(1, 10).min();
    OptionalDouble avg = IntStream.range(1, 10).average();

    // 짝수 숫자의 총합
    int evenSum = IntStream.range(1, 10)
        .filter(v -> ((v % 2) == 0))
        .sum();

    System.out.println(evenSum);
  }

  @Test
  void test13() {
    Set<Integer> evenNumber = IntStream.range(1, 1000).boxed()
        .filter(n -> (n%2 == 0))
        .collect(Collectors.toSet());
    System.out.println(evenNumber);
  }

  @Test
  void test14() {
    List<String> fruit = Arrays.asList("Banana", "Apple", "Melon");
    String returnValue = fruit.stream()
        .collect(Collectors.joining());

    System.out.println(returnValue);

    List<String> fruit1 = Arrays.asList("Banana", "Apple", "Melon");
    String returnValue1 = fruit.stream()
        .collect(Collectors.joining(",", "<", ">"));

    System.out.println(returnValue1);
  }

  @Test
  void test15() {
    IntStream.range(1, 1000).boxed()
        .filter(n -> (n%2 == 0))
        .forEach(System.out::println);
  }
}
