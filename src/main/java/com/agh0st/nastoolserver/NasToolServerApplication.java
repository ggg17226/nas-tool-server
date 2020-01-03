package com.agh0st.nastoolserver;

import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@MapperScan("com.agh0st.nastoolserver.Mapper")
@Log4j2
@ImportResource(locations = {"classpath:kaptcha.xml"})
public class NasToolServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(NasToolServerApplication.class, args);
  }
}
