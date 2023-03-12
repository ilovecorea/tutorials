package com.example.javalang;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.junit.jupiter.api.Test;

public class Generic1Tests {

  @Test
  public void test1() {
    List list1 = List.of(1, 2, 3);//row type List 선언
    list1.add("a");//런타임에 에러가 검출된다.

    List<Integer> list2 = List.of(1, 2, 3);
//    list2.add("a");//컴파일시 에러가 검출된다.

    List<Object> list3 = List.of(1, 2, 3);
    List<Object> list4 = list3;
//    List<Integer> list4 = list3;//List<타입> List<Object> 다름

    List<?> list5 = List.of(1, 2, 3);
//    List<Integer> list6 = list5;//List<타입>과 List<?> 다름
//    List<Object> list7 = list5;//List<Object>와 List>?> 다름
  }

  class StackExt1<E> extends Stack<E> {
    public void pushAll(Iterable<E> src) {
      for (E e : src) {
        push(e);
      }
    }
    public void popAll(Collection<E> dst) {
      while (!isEmpty()) {
        dst.add(pop());
      }
    }
  }

  class StackExt2<E> extends Stack<E> {
    public void pushAll(Iterable<? extends E> src) {
      for (E e : src) {
        push(e);
      }
    }
    public void popAll(Collection<? super E> dst) {
      while (!isEmpty()) {
        dst.add(pop());
      }
    }
  }

  @Test
  public void test2() {
    StackExt1<Number> stack1 = new StackExt1<>();
    Iterable<Integer> integers = List.of(1,2,3);
    //오류
//    stack1.pushAll(integers);
    Collection<Object> objects = List.of();
    //오류
//    stack1.popAll(objects);

    StackExt2<Number> stack2 = new StackExt2<>();
    stack2.pushAll(integers);
    stack2.popAll(objects);

  }

  @Test
  public void test3() {
    List<Integer> list1 = Arrays.asList(1,2,3);
    List<Number> list2 = Arrays.asList(4,5,6);
    Collections.copy(list2, list1);
    System.out.println(list2);
  }

}