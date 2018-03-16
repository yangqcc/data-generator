package com.yangqc.dg.config;

import java.util.concurrent.ForkJoinPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * <p>title:</p>
 * <p>description:</p>
 *
 * @author yangqc
 * @date Created in 2017-11-03
 * @modified By yangqc
 */
@Configuration
public class TaskConfig {

  @Bean(name = "forkJoinPool")
  @Scope(scopeName = "singleton")
  public ForkJoinPool getForkJoinPool() {
    return new ForkJoinPool();
  }
}
