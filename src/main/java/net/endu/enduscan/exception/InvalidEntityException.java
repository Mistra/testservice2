package net.endu.enduscan.exception;

public class InvalidEntityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidEntityException() {}
    
    public InvalidEntityException(String msg) {
        super(msg);
    }
}