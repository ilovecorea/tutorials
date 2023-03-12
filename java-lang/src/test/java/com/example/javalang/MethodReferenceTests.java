package com.example.javalang;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

public class MethodReferenceTests {

  public static class Printer {

    static void print(String s) {
      System.out.println(s);
    }
  }

  @FunctionalInterface
  interface Executable {

    void execute(String s);
  }

  public static class Company {

    String name;

    public Company(String name) {
      this.name = name;
    }

    public void print() {
      System.out.println(name);
    }
  }

  @Test
  void test1() {
    Consumer<String> fn = s -> System.out.println(s);
    fn.accept("Hello");
    Consumer<String> fn1 = System.out::println;
    fn1.accept("Hello");
  }

  @Test
  void test2() {
    Executable ex1 = s -> Printer.print(s);
    //static method reference
    Executable ex2 = Printer::print;
    ex1.execute("Hello");
    ex2.execute("Hello");
  }

  @Test
  void test3() {
    Consumer<String> ex1 = s -> Printer.print(s);
    Consumer<String> ex2 = Printer::print;
    ex1.accept("Hello");
    ex2.accept("Hello");
  }

  @Test
  void test4() {
    List<String> companies = Arrays.asList("google", "apple", "samsung");
    companies.stream().forEach(s -> System.out.println(s));
    companies.stream().forEach(System.out::println);
  }

  @Test
  void test5() {
    List<Company> companies = List.of(new Company("google"), new Company("apple"),
        new Company("samsung"));
    companies.stream().forEach(Company::print);
  }

  @Test
  void test6() {
    List<String> companies = Arrays.asList("google", "apple", "samsung");
    companies.stream()
        .mapToInt(String::length)
        .forEach(System.out::println);
  }

  @Test
  void test7() {
    List<String> companies = List.of("google", "apple", "samsung");
    companies.stream()
        .map(Company::new)
        .forEach(Company::print);
  }
}
