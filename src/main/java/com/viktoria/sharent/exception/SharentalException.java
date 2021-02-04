package com.viktoria.sharent.exception;

public class SharentalException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SharentalException(String ex) {
        super(ex);
    }

    public SharentalException(Exception e) {
        super(e);
    }
}
