package kz.dpoj.tcriptobot.model.birja;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public class ChartMessage extends SendMessage {
    public ChartMessage(SendPhoto sendPhoto) {
        this.sendPhoto = sendPhoto;
    }

    private SendPhoto sendPhoto;

    public SendPhoto getSendPhoto() {
        return sendPhoto;
    }

    public void setSendPhoto(SendPhoto sendPhoto) {
        this.sendPhoto = sendPhoto;
    }
}
