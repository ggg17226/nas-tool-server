package com.agh0st.nastoolserver.object.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * email_check
 *
 * @author
 */
@Data
public class EmailCheck implements Serializable {
  private Long id;

  private Long uid;

  private String targetEmail;

  private Date createTime;

  private Date updateTime;

  private String code;

  /** 0-待发送 1-已发送 2-已验证 3-已过期 4-发送失败 */
  private Integer statusCode;

  private Date sendTime;

  private Date verifyTime;

  private static final long serialVersionUID = 1L;
}
