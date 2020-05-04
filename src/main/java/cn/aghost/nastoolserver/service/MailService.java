package cn.aghost.nastoolserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class MailService {

  @Resource private JavaMailSender mailSender;
  @Resource private TemplateEngine templateEngine;

  @org.springframework.beans.factory.annotation.Value("${app.mail.username}")
  private String emailSender;

  /**
   * 发送提醒邮件
   *
   * @param target
   * @param subject
   * @param event
   * @param eventDate
   * @param details
   * @throws MessagingException
   */
  public void sendNotifyEmail(
      @NotNull String target,
      @NotNull String subject,
      @NotNull String event,
      @NotNull Date eventDate,
      @NotNull String details)
      throws MessagingException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Context context = new Context();
    context.setVariable("notifyDate", dateFormat.format(new Date()));
    context.setVariable("notifyEvent", event);
    context.setVariable("eventDate", dateFormat.format(eventDate));
    context.setVariable("notifyDetails", details);
    String content = templateEngine.process("mail/notify", context);

    sendEmail(target, subject, content);
  }

  /**
   * 发送绑定邮件
   *
   * @param target
   * @param sendDate
   * @param url
   * @throws MessagingException
   */
  public void sendBindEmail(@NotNull String target, @NotNull String sendDate, @NotNull String url)
      throws MessagingException {
    Context context = new Context();
    context.setVariable("sendDate", sendDate);
    context.setVariable("url", url);
    String content = templateEngine.process("mail/bind", context);

    sendEmail(target, "绑定邮箱", content);
  }

  private void sendEmail(String target, String subject, String content) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    mimeMessage.setFrom(emailSender);
    message.setTo(target);
    message.setSubject(subject);
    message.setText(content, true);
    mailSender.send(mimeMessage);
  }
}
