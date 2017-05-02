package com.afmobi.palmcall.exception;

import com.jtool.codegenannotation.CodeGenExceptionDefine;

@CodeGenExceptionDefine(code="-76", desc="该账号被重新登入，该session无效")
public class NotValideSessionException extends Exception {
    private static final long serialVersionUID = -4682126161234816072L;
}
