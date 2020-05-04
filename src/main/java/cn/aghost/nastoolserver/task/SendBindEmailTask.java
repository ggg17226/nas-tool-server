package cn.aghost.nastoolserver.task;

import cn.aghost.nastoolserver.mapper.EmailCheckMapper;
import cn.aghost.nastoolserver.object.entity.EmailCheck;
import cn.aghost.nastoolserver.service.MailService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SendBindEmailTask {
  private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  @Resource private EmailCheckMapper emailCheckMapper;
  @Resource private MailService mailService;
  @Value("${app.baseUrl}")
  private String baseUrl;

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
