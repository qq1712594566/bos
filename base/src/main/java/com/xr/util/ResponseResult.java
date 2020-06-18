package com.xr.util;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseResult {
    private Integer code = 20000; // 返回到前端的状态码，20000表示成功
    private Map<String,Object> data = new HashMap<>(); // 返回到前端的数据
    private String message = "默认的错误信息";
}
