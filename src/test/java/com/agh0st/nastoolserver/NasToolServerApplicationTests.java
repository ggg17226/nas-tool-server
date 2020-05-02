package com.agh0st.nastoolserver;

import com.agh0st.nastoolserver.service.MailService;
import com.agh0st.nastoolserver.utils.AliPush;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@ImportResource(locations = {"classpath:kaptcha.xml"})
@Slf4j
public class NasToolServerApplicationTests {
  @Resource MailService mailService;
  @Resource AliPush aliPush;
  @Resource private JavaMailSender mailSender;
  @Resource private TemplateEngine templateEngine;

  @org.springframework.beans.factory.annotation.Value("${app.mail.username}")
  private String emailSender;

  @Test
  public void getDeviceList() {
    log.info(JSON.toJSONString(aliPush.getDevices("test")));
  }

  @Test
  public void contextLoads() {}

  @Test
  public void testSendMail() throws MessagingException {
    mailService.sendNotifyEmail(
        "ggg17226@gmail.com",
        "测试邮件的标题",
        "通知事件",
        new Date(System.currentTimeMillis() - 100000),
        "通知\n详情");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Context context = new Context();
    context.setVariable("notifyDate", dateFormat.format(new Date()));
    context.setVariable("notifyEvent", "事件");
    StringJoiner stringJoiner = new StringJoiner("\n");
    IntStream.range(0, 100).forEach(i -> stringJoiner.add(UUID.randomUUID().toString()));
    context.setVariable("notifyDetails", "详情？？？？？" + stringJoiner.toString());
    String content = templateEngine.process("mail/notify", context);

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    mimeMessage.setFrom(emailSender);
    message.setTo("ggg17226@gmail.com");
    message.setSubject("服务器通知");
    message.setText(content, true);
    mailSender.send(mimeMessage);
  }
}
