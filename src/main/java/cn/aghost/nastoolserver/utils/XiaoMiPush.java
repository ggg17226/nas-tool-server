package cn.aghost.nastoolserver.utils;

import cn.aghost.nastoolserver.exception.XiaomiPushException;
import com.alibaba.fastjson.JSON;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.*;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Component
@Slf4j
public class XiaoMiPush {

  private String appId, appKey, appSecret;
  private Sender sender;
  private DevTools devTools;
  private String packageName;

  public XiaoMiPush(
      @Value("${app.mipush.appId}") @NotNull String appId,
      @Value("${app.mipush.appKey}") @NotNull String appKey,
      @Value("${app.mipush.appSecret}") @NotNull String appSecret,
      @Value("${app.mipush.packageName}") @NotNull String packageName) {
    this.appId = appId;
    this.appKey = appKey;
    this.appSecret = Base64Utils.encodeToUrlSafeString(Base64Utils.decodeFromString(appSecret));
    this.packageName = packageName;
    Constants.useOfficial();
    sender = new Sender(this.appSecret);
    devTools = new DevTools(this.appSecret);
  }

  public String sendToAccount(
      @NotBlank String accountUuid,
      @NotBlank String title,
      @NotBlank String payload,
      @NotBlank String desc)
      throws IOException, ParseException, XiaomiPushException {
    Result result = sender.sendToUserAccount(buildMessage(title, payload, desc), accountUuid, 3);
    ErrorCode errorCode = result.getErrorCode();
    if (errorCode.getValue() == ErrorCode.Success.getValue()) {
      return result.getMessageId();
    } else {
      throw new XiaomiPushException(JSON.toJSONString(result));
    }
  }

  private Message buildMessage(
      @NotBlank String title, @NotBlank String payload, @NotBlank String desc) {
    return new Message.Builder()
        .payload(payload)
        .title(title)
        .description(desc)
        .notifyType(-1)
        .restrictedPackageName(packageName)
        .build();
  }
}
