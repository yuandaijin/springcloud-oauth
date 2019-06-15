package com.jingcheng.user.common;

import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by Mr.kim on 2019/03/11.
 * Time:10:08
 * ProjectName:oauth
 */
@Data
public class RoleVo implements Serializable {
    private static final long serialVersionUID = 2179037393108205286L;
    private String code;
    private String name;
    private String description;
    public final static String DEFAULT_USERNAME = "system";
    private Long id = 0L;
    private String createdBy = DEFAULT_USERNAME;
    private String updatedBy = DEFAULT_USERNAME;
    private Date createdTime = Date.from(ZonedDateTime.now().toInstant());
    private Date updatedTime = Date.from(ZonedDateTime.now().toInstant());
}
