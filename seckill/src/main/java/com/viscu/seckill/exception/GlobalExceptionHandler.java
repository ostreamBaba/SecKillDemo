package com.viscu.seckill.exception;

import com.viscu.seckill.result.CodeMsg;
import com.viscu.seckill.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 全局拦截异常 拦截参数验证异常
 */

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException) e;
            e.printStackTrace();
            return Result.error(ex.getCm());
        }
        else if(e instanceof BindException){
            //获取绑定参数异常 手机验证
            BindException ex = (BindException) e;
            //获得所有的异常
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg)); //添加参数
        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }


    /*@ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
        if(e instanceof BindException){
            BindException ex = (BindException) e;
            //获得所有的异常
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg)); //添加参数
        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }*/

}
