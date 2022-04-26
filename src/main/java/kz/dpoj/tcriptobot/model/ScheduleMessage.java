package kz.dpoj.tcriptobot.model;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ScheduleMessage extends SendMessage {
    private String pair;

    private String cron;

    private String cronId;

    public ScheduleMessage(@NonNull String chatId, @NonNull String text, String pair, String cron, String cronId) {
        super(chatId, text);
        this.pair = pair;
        this.cron = cron;
        this.cronId = cronId;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getCronId() {
        return cronId;
    }

    public void setCronId(String cronId) {
        this.cronId = cronId;
    }
}
