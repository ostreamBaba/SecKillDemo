package com.viscu.seckill.exception;

import com.viscu.seckill.result.CodeMsg;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */
public class GlobalException extends RuntimeException{

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

}
