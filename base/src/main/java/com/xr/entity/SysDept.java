package com.xr.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
public class SysDept implements Serializable {
    private List<SysDept> items = new ArrayList<>();
    private Long id;
    private String name;
    private Long parentId;
    private Integer orderNum;
    private String createBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 将页面传过来的参数转换为后台的JAVA日期类型
    @JsonFormat(pattern = "yyyy-MM-dd") // 后台的JAVA类型，转为JSON字符串
    private Date createTime;
    private String lastUpdateBy;
    private Date lastUpdateTime;
    private Byte delFlag;
    private static final long serialVersionUID = 1L;

}
