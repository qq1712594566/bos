package com.xr.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SysRole implements Serializable {
    private List<SysMenu> menus = new ArrayList<>(); // 角色对应的权限


    private Long id;

    private String name;

    private String remark;

    private String createBy;

    private Date createTime;

    private String lastUpdateBy;

    private Date lastUpdateTime;

    private Byte delFlag;

    private static final long serialVersionUID = 1L;

}
