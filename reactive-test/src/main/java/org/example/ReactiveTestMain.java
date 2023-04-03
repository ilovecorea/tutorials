package org.example;

public class ReactiveTestMain {

  public static void main(String[] args) {
    MyPub pub = new MyPub();//출판자 생성
    MySub sub = new MySub();//구독자 생성
    pub.subscribe(sub);
  }
}