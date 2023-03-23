package org.example.petclinic;

import java.util.function.Consumer;

public interface Callback<T> {

  static <T> Callback<T> of(Consumer<T> success, Consumer<Throwable> failure) {
    return new DelegatingCallback<>(success, failure);
  }

  void success(T t);

  void failure(Throwable throwable);

  class DelegatingCallback<T> implements Callback<T> {
    private final Consumer<T> success;
    private final Consumer<Throwable> failure;

    public DelegatingCallback(Consumer<T> success, Consumer<Throwable> failure) {
      this.success = success;
      this.failure = failure;
    }

    public void success(T t) {
      success.accept(t);
    }

    public void failure(Throwable throwable) {
      failure.accept(throwable);
    }
  }
}
