package com.example.javalang;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class ReactiveStreamTest {

  public static class PublisherImpl implements Publisher<Integer> {

    @Override
    public void subscribe(Subscriber<? super Integer> subscriber) {
      Queue<Integer> queue = new LinkedList<>();
      IntStream.range(0, 10).forEach(queue::add);

      subscriber.onSubscribe(new Subscription() {
        @Override
        public void request(long n) {
          System.out.println("request:" + n);
          for (int i = 0; i < n; i++) {
            if (queue.isEmpty()) {
              subscriber.onComplete();
              return;
            }
            subscriber.onNext(queue.poll());
          }
        }

        @Override
        public void cancel() {
          System.out.println("publish cancel");
        }
      });
    }
  }

  public static class SubscriberImpl implements Subscriber<Integer> {

    private Subscription subscription;
    private long requestSize = 2;
    private List<Integer> buffer = new ArrayList<>();

    @Override
    public void onSubscribe(Subscription s) {
      subscription = s;
      subscription.request(requestSize);
    }

    @Override
    public void onNext(Integer integer) {
      System.out.println(" onNext - " + integer);
      buffer.add(integer);
      if (buffer.size() == requestSize) {
        buffer.clear();
        subscription.request(requestSize);
      }
    }

    @Override
    public void onError(Throwable t) {
      System.out.println("error:" + t.getMessage());
    }

    @Override
    public void onComplete() {
      System.out.println("subscribe complete");
    }
  }

  @Test
  void test() {
    Publisher<Integer> publisher = new PublisherImpl();
    publisher.subscribe(new SubscriberImpl());
  }

}
