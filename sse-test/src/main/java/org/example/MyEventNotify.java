package org.example;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MyEventNotify {

  private List<String> events = new ArrayList<>();
  private boolean change = false;

  public void add(String data) {
    events.add(data);
    change = true;
  }

  public String get() {
    String data =  events.get(events.size() - 1);
    change = false;
    return data;
  }
}
