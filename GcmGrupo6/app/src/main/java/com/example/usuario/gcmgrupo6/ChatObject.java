package com.example.usuario.gcmgrupo6;

/**
 * Created by Usuario on 09/12/2015.
 */
public class ChatObject {
    String message;
    String type;

    public ChatObject(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
