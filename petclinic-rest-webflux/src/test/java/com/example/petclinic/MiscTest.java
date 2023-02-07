package com.example.petclinic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MiscTest {

  private static final Logger log = LoggerFactory.getLogger(MiscTest.class);

  @Test
  public void test() {

    Mono<String> str = Mono.empty();
    str
        .flatMap(s1 -> {
          log.debug("s1 flatMap: {}", s1);
          return Mono.just(s1);
        })
        .map(s1 -> {
          log.debug("s1 map: {}", s1);
          return s1;
        })
        .subscribe();

    str
        .switchIfEmpty(Mono.just("string"))
        .flatMap(s2 -> {
          log.debug("s2 flatMap: {}", s2);
          return Mono.just(s2);
        })
        .map(s2 -> {
          log.debug("s2 map: {}", s2);
          return s2;
        })
        .subscribe();

    str
        .flatMap(s3 -> {
          log.debug("s3 flatMap: {}", s3);
          return Mono.just(s3);
        })
        .switchIfEmpty(Mono.just("string"))
        .map(s3 -> {
          log.debug("s3 map: {}", s3);
          return s3;
        })
        .subscribe();
  }

  @Test
  public void test2() {
    Optional<String> s = Optional.of("test");
    assertEquals(Optional.of("TEST"), s.map(String::toUpperCase));

    assertEquals(Optional.of(Optional.of("STRING")),
        Optional
            .of("string")
            .map(s1 -> Optional.of("STRING")));

    assertEquals(Optional.of("STRING"), Optional
        .of("string")
        .flatMap(s1 -> Optional.of("STRING")));
  }

  @Test
  public void test3() {
    List<String> myList = Stream.of("a", "b")
        .map(String::toUpperCase)
        .collect(Collectors.toList());
    assertEquals(List.of("A", "B"), myList);

    List<List<String>> list = Arrays.asList(
        Arrays.asList("a"),
        Arrays.asList("b"));
    System.out.println(list);

    System.out.println(list
        .stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList()));
  }

  @Test
  public void test4() {
    Function<String, String> mapper = String::toUpperCase;
    Flux<String> inFlux = Flux.just("baeldung", ".", "com");
    Flux<String> outFlux = inFlux.map(mapper);
    StepVerifier.create(outFlux)
        .expectNext("BAELDUNG", ".", "COM")
        .expectComplete()
        .verify();
  }

  @Test
  public void test5() {
    Function<String, Publisher<String>> mapper = s -> Flux.just(s.toUpperCase().split(""));
    Flux<String> inFlux = Flux.just("baeldung", ".", "com");
    Flux<String> outFlux = inFlux.flatMap(mapper);
    List<String> output = new ArrayList<>();
    outFlux.subscribe(output::add);
    assertThat(output).containsExactlyInAnyOrder("B", "A", "E", "L", "D", "U", "N", "G", ".", "C", "O", "M");
  }
}
