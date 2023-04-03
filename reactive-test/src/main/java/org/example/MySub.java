package org.example;

import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MySub implements Subscriber<Integer> {

  private Subscription s;
  private int requestSize = 3;
  private List<Integer> buffer = new ArrayList<>();

  @Override
  public void onSubscribe(Subscription s) {
    System.out.println("구독 정보 수신 완료");
    this.s = s;
    System.out.println("구독 정보 전송 시작");
    s.request(requestSize);//한번에 구독할 개수를 지정
  }

  @Override
  public void onNext(Integer t) {
    System.out.println("onNext():" + t);
    buffer.add(t);
    if (buffer.size() == requestSize) {
      buffer.clear();
      s.request(requestSize);
    }
  }

  @Override
  public void onError(Throwable t) {
    System.out.println("구독중 에러");
  }

  @Override
  public void onComplete() {
    System.out.println("구독 완료");
  }
}
