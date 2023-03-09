package com.example.javalang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

public class GenericTests {

  private static final Logger log = LoggerFactory.getLogger(GenericTests.class);

  static <T> List<T> fromArrayToList(T[] a) {
    return Arrays.stream(a).collect(Collectors.toList());
  }

  static <T, G> List<G> fromArrayToList(T[] a, Function<T, G> mapperFunction) {
    return Arrays.stream(a).map(mapperFunction).collect(Collectors.toList());
  }

  static <T extends Number> List<T> fromArrayToListWithUpperBound(T[] a) {
    return Arrays.stream(a).collect(Collectors.toList());
  }

  static class Building {
    public void paint() {
      System.out.println("Painting Building");
    }
  }

  static class House extends Building {
    public void paint() {
      System.out.println("Painting House");
    }
  }

  static void paintAllBuildings(List<? extends Building> buildings) {
    buildings.forEach(Building::paint);
  }

  @Test
  public void test1() {
    Integer[] intArray = {1, 2, 3, 4, 5};
    List<Integer> list = fromArrayToList(intArray);

    assertThat(list, hasItems(intArray));
  }

  @Test
  public void test2() {
    String[] stringArray = {"a", "b", "c", "d", "e"};
    List<String> list = fromArrayToList(stringArray);

    assertThat(list, hasItems(stringArray));
  }

  @Test
  public void test3() {
    Integer[] intArray = {1, 2, 3, 4, 5};
    List<String> stringList = fromArrayToList(intArray, Objects::toString);

    assertThat(stringList, hasItems("1", "2", "3", "4", "5"));
  }

  @Test
  public void test4() {
    Integer[] intArray = {1, 2, 3, 4, 5};
    List<Integer> list = fromArrayToListWithUpperBound(intArray);

    assertThat(list, hasItems(intArray));
  }

  @Test
  public void test5() {
    List<Building> subBuildingsList = new ArrayList<>();
    subBuildingsList.add(new Building());
    subBuildingsList.add(new House());
    paintAllBuildings(subBuildingsList);
  }
}
