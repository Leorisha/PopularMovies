package com.cryogenius.popularmovies.Bus.Messages.Events;

/**
 * Created by Ana Neto on 29/01/2017.
 */

public class ErrorEvent {

    private String title;
    private String message;
    private int code;

    public ErrorEvent(String title, String message, int code){
        this.code = code;
        this.message = message;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
