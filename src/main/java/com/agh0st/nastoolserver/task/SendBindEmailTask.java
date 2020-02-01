package com.agh0st.nastoolserver.task;

import com.agh0st.nastoolserver.mapper.EmailCheckMapper;
import com.agh0st.nastoolserver.object.entity.EmailCheck;
import com.agh0st.nastoolserver.service.MailService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Log4j2
public class SendBindEmailTask {
  @Resource private EmailCheckMapper emailCheckMapper;
  @Resource private MailService mailService;

  @Value("${app.baseUrl}")
  private String baseUrl;

  private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Scheduled(fixedDelay = 60000, initialDelay = 1000)
  public void sendBindEmail() {
    List<EmailCheck> emailChecks = emailCheckMapper.selectNeedSendData();
    Date sendDate = new Date();
    List<Long> successIdList = new ArrayList<>();
    List<Long> failIdList = new ArrayList<>();
    emailChecks.forEach(
        emailCheck -> {
          try {
            mailService.sendBindEmail(
                emailCheck.getTargetEmail(),
                df.format(sendDate),
                baseUrl + "/V1/user/checkBind/" + emailCheck.getCode());
            successIdList.add(emailCheck.getId());
          } catch (MessagingException e) {
            log.error("send_bind_email: {}", ExceptionUtils.getStackTrace(e));
            failIdList.add(emailCheck.getId());
          }
        });
    if (successIdList.size() > 0) emailCheckMapper.updateSentData(sendDate, successIdList);
    if (failIdList.size() > 0) emailCheckMapper.updateSendFailData(sendDate, failIdList);
  }

  @Scheduled(fixedDelay = 60000, initialDelay = 1000)
  public void setTimeout() {
    emailCheckMapper.updateTimeout(new Date((new Date()).getTime() - 3600000));
  }
}
