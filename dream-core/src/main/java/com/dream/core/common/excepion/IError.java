package com.dream.core.common.excepion;

/**
 * 异常接口
 */
public interface IError {

    String getNamespace();

    String getErrorCode();

    String getErrorMessage();
}
