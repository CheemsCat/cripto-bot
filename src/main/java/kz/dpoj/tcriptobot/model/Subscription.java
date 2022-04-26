package kz.dpoj.tcriptobot.model;

import org.springframework.data.annotation.Id;

public class Subscription {
    public Subscription(Long chatId, String pair, String birja, String cronId){
        this.chatId = chatId;
        this.pair = pair;
        this.birja = birja;
        this.cronId = cronId;
    }
    @Id
    private String id;

    private Long chatId;

    private String pair;

    private String birja;

    private String cronId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getBirja() {
        return birja;
    }

    public void setBirja(String birja) {
        this.birja = birja;
    }

    public String getCronId() {
        return cronId;
    }

    public void setCronId(String cronId) {
        this.cronId = cronId;
    }
}
