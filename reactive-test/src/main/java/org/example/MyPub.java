package org.example;

import java.util.Arrays;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class MyPub implements Publisher<Integer> {

  /**
   * 구독 데이터
   */
  Iterable<Integer> its = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

  @Override
  public void subscribe(Subscriber<? super Integer> s) {
    System.out.println("구독 신청 시작");
    System.out.println("구독 정보 생성중");
    //구독자와 구독 데이터로 구독 정보 생성
    MySubscription subscription = new MySubscription(s, its);
    s.onSubscribe(subscription);
  }

}
