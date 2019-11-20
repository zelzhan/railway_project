package main.wrappers;

public class Message {
    String login;
    String data;
    String message;

    public Message(String login, String data, String msg) {
        this.login = login;
        this.data = data;
        this.message = msg;
    }
}