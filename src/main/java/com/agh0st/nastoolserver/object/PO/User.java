package com.agh0st.nastoolserver.object.PO;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * user
 * @author 
 */
@Data
public class User implements Serializable {
    private Integer id;

    private String username;

    private String passwd;

    private String salt;

    private Date createTime;

    private Date updateTime;

    private String uuid;

    private static final long serialVersionUID = 1L;
}