package com.lahhass.miaosha.exception;

import com.lahhass.miaosha.result.CodeMsg;
import com.sun.org.apache.bcel.internal.classfile.Code;


public class GlobalException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

}
