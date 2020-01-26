package com.agh0st.nastoolserver.object.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * user
 *
 * @author
 */
@Data
public class User implements Serializable {
  private Long id;

  private String username;

  private String passwd;

  private String salt;

  private Date createTime;

  private Date updateTime;

  private String uuid;

  private String email;

  /** 0-未验证 1-已验证 */
  private Integer emailChecked;

  private static final long serialVersionUID = 1L;
}
