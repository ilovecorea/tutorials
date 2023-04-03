package org.example;

import java.util.Iterator;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 구독정보(구독자, 구독 데이터)
 */
public class MySubscription implements Subscription {

  private Subscriber s;
  private Iterator it;

  public MySubscription(Subscriber<? super Integer> s, Iterable<Integer> its) {
    this.s = s;
    this.it = its.iterator();
    System.out.println("구독 정보 생성 완료");
  }

  @Override
  public void request(long n) {
    while (n > 0) {
      if (it.hasNext()) {
        s.onNext(it.next());
      } else {
        s.onComplete();
        break;
      }
      n--;
    }
  }

  @Override
  public void cancel() {

  }
}
