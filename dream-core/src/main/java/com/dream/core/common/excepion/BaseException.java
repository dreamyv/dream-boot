package com.dream.core.common.excepion;

/**
 * 自定义异常类
 */
public class BaseException extends RuntimeException{
    private static final long serialVersionUID = -6293662498600553602L;
    private IError error;
    private String extMessage;

    public BaseException() {
        this.error = BaseError.SYSTEM_INTERNAL_ERROR;
        this.extMessage = null;
    }

    public BaseException(String message) {
        super(message);
        this.error = BaseError.SYSTEM_INTERNAL_ERROR;
        this.extMessage = null;
        this.extMessage = message;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.error = BaseError.SYSTEM_INTERNAL_ERROR;
        this.extMessage = null;
        this.extMessage = message;
    }

    public BaseException(Throwable cause) {
        super(cause);
        this.error = BaseError.SYSTEM_INTERNAL_ERROR;
        this.extMessage = null;
        if (cause instanceof BaseException) {
            BaseException fe = (BaseException)cause;
            this.error = fe.getError();
            this.extMessage = fe.getMessage();
        }

    }

    public BaseException(IError error) {
        super(error.getErrorCode() + ":" + error.getErrorMessage());
        this.error = BaseError.SYSTEM_INTERNAL_ERROR;
        this.extMessage = null;
        this.error = error;
    }

    public BaseException(IError error, String message) {
        super(error.getErrorCode() + ":" + error.getErrorMessage());
        this.error = BaseError.SYSTEM_INTERNAL_ERROR;
        this.extMessage = null;
        this.error = error;
        this.extMessage = message;
    }

    public BaseException(Throwable cause, IError error, String message) {
        super(message, cause);
        this.error = BaseError.SYSTEM_INTERNAL_ERROR;
        this.extMessage = null;
        this.extMessage = message;
        this.error = error;
    }

    public BaseException(IError error, Throwable cause) {
        super(cause);
        this.error = BaseError.SYSTEM_INTERNAL_ERROR;
        this.extMessage = null;
        this.error = error;
    }

    public IError getError() {
        return this.error;
    }

    public String getExtMessage() {
        return this.extMessage;
    }

    public void setExtMessage(String extMessage) {
        this.extMessage = extMessage;
    }

    public String toString() {
        return super.toString() + ",ErrorCode : " + this.error.getErrorCode() + ", ErrorMessage : " + this.error.getErrorMessage() + ", ExtMessage : " + this.extMessage;
    }
}
