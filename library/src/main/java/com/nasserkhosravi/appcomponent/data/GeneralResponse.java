package com.nasserkhosravi.appcomponent.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * In most project to fetching API I use this generic structure class
 */
public class GeneralResponse<T> {

    /**
     * Status of operation
     * e.g: client
     */
    @Expose
    @SerializedName("status")
    private boolean status;

    /**
     * A message from backend developer to clint developer
     * This can be use as debug or showing a message from backend to client
     */
    @Expose
    @SerializedName("message")
    private String message;

    /**
     * A custom response object
     * This object change service to another service
     */
    private T response;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
