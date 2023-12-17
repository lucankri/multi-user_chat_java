package edu.school21.sockets.models;

public class Message {
    private Long id;
    private User sender;
    private String text;
    private String date;

    public Message() {}

    public Message(User sender, String text, String date) {
        this.sender = sender;
        this.text = text;
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
