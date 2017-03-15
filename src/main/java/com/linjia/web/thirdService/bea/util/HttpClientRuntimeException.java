package com.linjia.web.thirdService.bea.util;

/**
 * Http 请求异常
 */
public class HttpClientRuntimeException extends RuntimeException {
    public HttpClientRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
