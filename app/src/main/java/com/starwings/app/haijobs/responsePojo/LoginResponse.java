package com.starwings.app.haijobs.responsePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.User;

import java.util.List;

public class LoginResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user")
    @Expose
    private List<User> user = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
