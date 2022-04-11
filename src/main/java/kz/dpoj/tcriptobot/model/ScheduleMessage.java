package kz.dpoj.tcriptobot.model;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ScheduleMessage extends SendMessage {
    private String pair;

    public ScheduleMessage(@NonNull String chatId, @NonNull String text, String pair) {
        super(chatId, text);
        this.pair = pair;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }
}
