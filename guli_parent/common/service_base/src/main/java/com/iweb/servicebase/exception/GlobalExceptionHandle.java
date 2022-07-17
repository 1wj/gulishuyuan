package com.iweb.servicebase.exception;


import com.iweb.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/*
    统一异常处理类
 */
@ControllerAdvice  //加个注解统一异常处理类
@Slf4j   //logback日志注解
public class GlobalExceptionHandle {

    //全局异常  执行这个方法
    @ExceptionHandler(Exception.class)
    //为了返回json数据
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理");
    }

    //特定异常
    @ExceptionHandler(ArithmeticException.class)
    //为了返回json数据
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理");
    }

    //自定义异常
    @ExceptionHandler(GuLiException.class)
    //为了返回json数据
    @ResponseBody
    public R error(GuLiException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

}
