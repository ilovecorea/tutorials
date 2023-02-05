package com.example.petclinic;

import io.r2dbc.spi.Parameter.In;
import java.time.Duration;
import java.util.Random;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

public class ReactorTest {

  private static final Logger log = LoggerFactory.getLogger(ReactorTest.class);

  @Test
  public void test1() {
    //doOnNext() 메서드는 subscriber에 next 신호를 발생할 때 호출된다.
    Flux.just(1,2,3)
        .doOnNext(i -> log.debug("doOnNext:{}", i))
        .subscribe(i -> log.debug("Received:{}", i));
  }

  @Test
  public void test2() {
    Flux<Integer> seq = Flux.just(1,2,3)
        .doOnNext(i -> log.debug("doOnNext:{}", i));
    log.debug("시퀀스 생성");
    seq.subscribe(i -> log.debug("Received:{}", i));
  }

  @Test
  public void test3() {
    //콜드 시퀀스는 구독을 할 때마다 새로운 요청을 전송한다.
    Flux<Integer> seq = Flux.just(1,2,3);
    seq.subscribe(v -> log.debug("구독1:{}", v));
    seq.subscribe(v -> log.debug("구독2:{}", v));
  }

  @Test
  public void test4() {
    Flux<Integer> seq = Flux.just(1,2,3);
    seq.subscribe(new Subscriber<Integer>() {

      private Subscription subscription;

      @Override
      public void onSubscribe(Subscription subscription) {
        log.debug("Subscriber.onSubscribe");
        this.subscription = subscription;
        this.subscription.request(1);//publisher에 데이터 요청
      }

      @Override
      public void onNext(Integer integer) {
        log.debug("Subscriber.onNext:{}", integer);
        this.subscription.request(1);//publisher에 데이터 요청
      }

      @Override
      public void onError(Throwable throwable) {
        log.debug("Subscriber.onError:{}", throwable.getMessage());
      }

      @Override
      public void onComplete() {
        log.debug("Subscriber.onComplete");
      }
    });
  }

  @Test
  public void test5() {
    Flux<Integer> seq = Flux.just(1, 2, 3);
    seq.subscribe(new Subscriber<>() {
      private Subscription subscription;
      @Override
      public void onSubscribe(Subscription s) {
        log.debug("Subscriber.onSubscribe");
        this.subscription = s;
        this.subscription.request(Long.MAX_VALUE);
      }

      @Override
      public void onNext(Integer i) {
        log.debug("Subscriber.onNext: " + i);
      }

      @Override
      public void onError(Throwable throwable) {
        log.debug("Subscriber.onError:{}", throwable.getMessage());
      }

      @Override
      public void onComplete() {
        log.debug("Subscriber.onComplete");
      }
    });
  }

  @Test
  public void test6() {
    Consumer<SynchronousSink<Integer>> randGen = new Consumer<SynchronousSink<Integer>>() {
      private int emitCount = 0;
      private Random rand = new Random();

      @Override
      public void accept(SynchronousSink<Integer> sink) {
        emitCount++;
        int data = rand.nextInt(100) + 1;//1에서 100 사이 임의 정수
        log.debug("Generator sink next {}", data);
        sink.next(data);//임의 정수 데이터 발생
        if (emitCount == 10) {//10개 데이터를 발생했으면
          log.debug("Generator sink complete");
          sink.complete();//완료 신호 발생
        }
      }
    };

    Flux<Integer> seq = Flux.generate(randGen);

    seq.subscribe(new BaseSubscriber<>() {
      private int receiveCount = 0;

      @Override
      protected void hookOnSubscribe(Subscription subscription) {
        log.debug("Subscriber#onSubscribe");
        log.debug("Subscriber request first 3 items");
        request(3);
      }

      @Override
      protected void hookOnNext(Integer value) {
        log.debug("Subscriber#onNext:{}", value);
        receiveCount++;
        if (receiveCount % 3 == 0) {
          log.debug("Subscriber request next 3 items");
          request(3);
        }
      }

      @Override
      protected void hookOnComplete() {
        log.info("Subscriber#onComplete");
      }
    });
  }

  @Test
  public void test7() {
    Flux.just("a", "bc", "def", "wxyz")
        .map(s -> s.length())//문자열을 integer 값으로 1-1 변환
        .subscribe(len -> log.debug("len:{}", len));
  }

  @Test
  public void test8() {
    Flux<Integer> seq = Flux.just(1,2,3)
        .flatMap(i -> Flux.range(1, i));//Integer를 Flux<Integer>로 1-n 변환

    seq.subscribe(System.out::println);
  }

  @Test
  public void test9() {
    Flux.range(1, 10)
        .filter(num -> num % 2 == 0)
        .subscribe(x -> log.debug("{} -> ", x));
  }

  @Test
  public void test10() {
    Flux<Integer> seq1 = Flux.just(1,2,3);
    Flux<Integer> seq2 = seq1.startWith(-1, 0);
    seq2.subscribe(System.out::println);
  }

  @Test
  public void test11() {
    Flux<String> tick1 = Flux.interval(Duration.ofSeconds(1)).map(tick -> tick + "초틱");
    Flux<String> tick2 = Flux.interval(Duration.ofMillis(700)).map(tick -> tick + "밀리초틱");
    tick1.zipWith(tick2).subscribe(tup -> log.debug("{}", tup));
  }
}
