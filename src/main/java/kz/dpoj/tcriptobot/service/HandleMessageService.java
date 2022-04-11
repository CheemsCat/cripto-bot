package kz.dpoj.tcriptobot.service;

import kz.dpoj.tcriptobot.buttons.InlineKeyboardMaker;
import kz.dpoj.tcriptobot.buttons.ReplyKeyboardMaker;
import kz.dpoj.tcriptobot.model.ButtonNameEnum;
import kz.dpoj.tcriptobot.model.ScheduleMessage;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.UUID;

@Service
public class HandleMessageService {
    private InlineKeyboardMaker inlineKeyboardMaker;
    private ReplyKeyboardMaker replyKeyboardMaker;
    private SendMessageBuilder sendMessageBuilder;

    public HandleMessageService(InlineKeyboardMaker inlineKeyboardMaker, ReplyKeyboardMaker replyKeyboardMaker,
                                SendMessageBuilder sendMessageBuilder){
        this.inlineKeyboardMaker = inlineKeyboardMaker;
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.sendMessageBuilder = sendMessageBuilder;
    }

    public SendMessage handleMessage(Long chatId, String messageText){
        if(messageText.equals(ButtonNameEnum.GET_PRICE.getButtonName())){
            return sendMessageBuilder.buildSendMessage(chatId, "Выберите биржу", inlineKeyboardMaker.getKeyboard(false));
        } else if(messageText.equals(ButtonNameEnum.ADD_SUBSCRIBE.getButtonName())){
            return sendMessageBuilder.buildSendMessage(chatId, "Выберите биржу", inlineKeyboardMaker.getKeyboard(true));
        } else if(messageText.equals(ButtonNameEnum.HELP_BUTTON.getButtonName())){
            return null;
        } else{
            return sendMessageBuilder.buildSendMessage(chatId, "Выберите кнопку!", replyKeyboardMaker.getKeyboard());
        }
    }
}
