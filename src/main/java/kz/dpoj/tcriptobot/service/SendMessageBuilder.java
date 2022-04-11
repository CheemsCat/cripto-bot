package kz.dpoj.tcriptobot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Service
public class SendMessageBuilder {
    public SendMessage buildSendMessage(Long chatId, String messageText, ReplyKeyboard...keyboard){
        SendMessage sendMessage = new SendMessage(chatId.toString(), messageText);
        for(ReplyKeyboard r : keyboard) {
            sendMessage.setReplyMarkup(r);
        }
        return sendMessage;
    }
}
