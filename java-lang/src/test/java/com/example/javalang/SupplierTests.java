package com.example.javalang;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

public class SupplierTests {

  @Test
  public void test1() {
    Supplier<LocalDateTime> s = () -> LocalDateTime.now();
    LocalDateTime time = s.get();
    System.out.println(time);

    Supplier<String> s1 = () -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .format(LocalDateTime.now());
    String time2 = s1.get();
    System.out.println(time2);
  }
}
