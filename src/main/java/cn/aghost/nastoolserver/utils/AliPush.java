package cn.aghost.nastoolserver.utils;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.push.model.v20160801.QueryDevicesByAccountRequest;
import com.aliyuncs.push.model.v20160801.QueryDevicesByAccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AliPush {
  private DefaultProfile profile = null;
  private IAcsClient client = null;

  @Value("${app.alipush.appKey}")
  private String appKey;

  @Value("${app.alipush.appSecret}")
  private String appSecret;

  public AliPush(
      @Value("${app.alipush.region}") String region,
      @Value("${app.alipush.accessKey}") String accessKey,
      @Value("${app.alipush.secret}") String secret) {

    profile = DefaultProfile.getProfile(region, accessKey, secret);
    client = new DefaultAcsClient(profile);
  }

  public JSONArray getDevices(String uuid) {
    QueryDevicesByAccountRequest qdbaReq = new QueryDevicesByAccountRequest();
    qdbaReq.setAppKey(Long.valueOf(appKey));
    qdbaReq.setAccount(uuid);
    QueryDevicesByAccountResponse qdbaRsp = null;
    List<String> deviceIds = null;
    try {
      qdbaRsp = client.getAcsResponse(qdbaReq);
      if (qdbaRsp != null) {
        deviceIds = qdbaRsp.getDeviceIds();
      }
    } catch (Exception e) {
      qdbaRsp = null;
      deviceIds = null;
    }
    JSONArray devices = new JSONArray();
    if (deviceIds != null && deviceIds.size() > 0) {
      deviceIds.forEach(deviceId -> devices.add(deviceId));
    }
    return devices;
  }

  public boolean pushToIos(
      String uuid, String pushType, String title, String body, String env, String payload) {
    if ((!"MESSAGE".equals(pushType)) && (!"NOTICE".equals(pushType))) {
      return false;
    }
    if ((!"DEV".equals(env)) && (!"PRODUCT".equals(env))) {
      return false;
    }
    PushRequest pushRequest = new PushRequest();
    pushRequest.setAppKey(Long.valueOf(appKey));
    pushRequest.setTarget("ACCOUNT");
    pushRequest.setTargetValue(uuid);
    pushRequest.setDeviceType("iOS");
    pushRequest.setPushType(pushType);
    pushRequest.setTitle(title);
    pushRequest.setBody(body);
    pushRequest.setIOSApnsEnv(env);
    pushRequest.setIOSExtParameters(payload);
    if ("PRODUCT".equals(env)) {
      pushRequest.setIOSRemind(true);
      pushRequest.setIOSRemindBody(body);
    }
    PushResponse pushResponse = null;
    String msgId = null;
    try {
      pushResponse = client.getAcsResponse(pushRequest);
      if (pushResponse != null) {
        msgId = pushResponse.getMessageId();
      }
    } catch (Exception e) {
      pushResponse = null;
      msgId = null;
    }
    return !StringUtils.isEmpty(msgId);
  }

  public boolean pushMessageToIos(
      String uuid, String title, String body, String env, String payload) {
    return pushToIos(uuid, "MESSAGE", title, body, env, payload);
  }

  public boolean pushNoticeToIos(
      String uuid, String title, String body, String env, String payload) {
    return pushToIos(uuid, "NOTICE", title, body, env, payload);
  }
}
