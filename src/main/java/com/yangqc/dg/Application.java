package com.yangqc.dg;

import com.yangqc.dg.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yangqc
 */
@EnableConfigurationProperties({ApplicationProperties.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication
@EnableScheduling
public class Application {

  /**
   * 启动应用
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
