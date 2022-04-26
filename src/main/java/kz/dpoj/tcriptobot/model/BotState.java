package kz.dpoj.tcriptobot.model;

import org.springframework.data.annotation.Id;

public class BotState {
    @Id
    private String id;

    private String state;

    private String birjaName;

    private Long chatId;

    private String pair;

    private String findPair;

    private String chartPair;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBirjaName() {
        return birjaName;
    }

    public void setBirjaName(String birjaName) {
        this.birjaName = birjaName;
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

    public String getFindPair() {
        return findPair;
    }

    public void setFindPair(String findPair) {
        this.findPair = findPair;
    }

    public String getChartPair() {
        return chartPair;
    }

    public void setChartPair(String chartPair) {
        this.chartPair = chartPair;
    }
}
