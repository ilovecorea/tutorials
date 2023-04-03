package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyFilter implements Filter {

  private final MyEventNotify eventNotify;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    System.out.println("필터 실행");
    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//    servletResponse.setContentType("text/plain; charset=utf-8");
    servletResponse.setContentType("text/event-stream; charset=utf-8");
    PrintWriter out = servletResponse.getWriter();
    for (int i = 0; i < 5; i++) {
      out.println("응답" + (i + 1));
      out.flush();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    while(true) {
      try {
        if (eventNotify.isChange()) {
          out.println(eventNotify.get());
          out.flush();
        }
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
