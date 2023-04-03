package org.example;

import javax.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MyFilterConfig {

  private final MyEventNotify eventNotify;

  @Bean
  public FilterRegistrationBean<Filter> addFilter() {
    System.out.println("필더 등록");
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter(eventNotify));
    bean.addUrlPatterns("/sse");
    return bean;
  }

  @Bean
  public FilterRegistrationBean<Filter> addFilter2() {
    System.out.println("필더 등록");
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter2(eventNotify));
    bean.addUrlPatterns("/add");
    return bean;
  }
}
