package com.iweb.servicebase.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                 //生成get set 方法
@AllArgsConstructor  //生成有参构造方法
@NoArgsConstructor  //生成无参构造
public class GuLiException extends RuntimeException{
    private Integer code;//状态码
    private String msg;//异常信息


}
