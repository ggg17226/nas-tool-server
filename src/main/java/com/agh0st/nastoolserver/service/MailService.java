package com.agh0st.nastoolserver.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MailService {

  @Resource private JavaMailSender mailSender;
  @Resource private TemplateEngine templateEngine;

  @org.springframework.beans.factory.annotation.Value("${app.mail.username}")
  private String emailSender;

  public void sendNotifyEmail(
      String target, String subject, String event, Date eventDate, String details)
      throws MessagingException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Context context = new Context();
    context.setVariable("notifyDate", dateFormat.format(new Date()));
    context.setVariable("notifyEvent", event);
    context.setVariable("eventDate", dateFormat.format(eventDate));
    context.setVariable("notifyDetails", details);
    String content = templateEngine.process("mail/notify", context);

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    mimeMessage.setFrom(emailSender);
    message.setTo(target);
    message.setSubject(subject);
    message.setText(content, true);
    mailSender.send(mimeMessage);
  }
}
