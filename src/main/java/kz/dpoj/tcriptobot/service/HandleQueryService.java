package kz.dpoj.tcriptobot.service;

import kz.dpoj.tcriptobot.buttons.ButtonTypeEnum;
import kz.dpoj.tcriptobot.buttons.InlineKeyboardMaker;
import kz.dpoj.tcriptobot.model.BirjaNameEnum;
import kz.dpoj.tcriptobot.model.ExmoData;
import kz.dpoj.tcriptobot.model.ScheduleMessage;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Service
public class HandleQueryService {
    private BirjaService birjaService;
    private InlineKeyboardMaker inlineKeyboardMaker;
    private SendMessageBuilder sendMessageBuilder;

    public HandleQueryService(BirjaService birjaService, InlineKeyboardMaker inlineKeyboardMaker,
                              SendMessageBuilder sendMessageBuilder){
        this.birjaService = birjaService;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
        this.sendMessageBuilder = sendMessageBuilder;
    }

    public BotApiMethod handleCallbackQuery(Long chatId, ButtonTypeEnum type, String query, Integer messageId) {
        switch (type) {
            case PAGE:
                return handlePage(chatId, query, messageId, false);
            case SUBSCRIBE_PAGE:
                return handlePage(chatId, query, messageId, true);
            case PAIR:
                return handlePair(chatId, query);
            case SUBSCRIBE_PAIR:
                return new ScheduleMessage(chatId.toString(), "Вы подписались на пару " + query, query);
            case BIRJA:
                return handleBirja(chatId, query, false);
            case SUBSCRIBE_BIRJA:
                return handleBirja(chatId, query, true);
        }

        return null;
    }

    public SendMessage handleBirja(Long chatId, String birja, boolean isSubscribe) {
        if(birja.equals(BirjaNameEnum.EXMO.getButtonName())){
            List<String> pairs = birjaService.getPairs();
            return sendMessageBuilder.buildSendMessage(chatId, "Выберите пару", inlineKeyboardMaker.getPairKeyboard(pairs, 1, isSubscribe));
        } else if(birja.equals(BirjaNameEnum.BINANCE.getButtonName())){

        } else{

        }

        return null;
    }

    public SendMessage handlePair(Long chatId, String pair) {
        ExmoData exmoData = birjaService.getDataByPair(pair);
        return sendMessageBuilder.buildSendMessage(chatId, "Цена " + pair + " - " + exmoData.getAmount());
    }

    private EditMessageReplyMarkup handlePage(Long chatId, String pageNumber, Integer messageId, boolean isSubscribe){
        List<String> pairs = birjaService.getPairs();
        return new EditMessageReplyMarkup(chatId.toString(), messageId, null, (InlineKeyboardMarkup) inlineKeyboardMaker.getPairKeyboard(pairs, Integer.parseInt(pageNumber), isSubscribe));
    }
}
