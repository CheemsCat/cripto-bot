package kz.dpoj.tcriptobot.service;

import kz.dpoj.tcriptobot.enumerate.ButtonTypeEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotProcessUpdate {
    private HandleQueryService handleQueryService;
    private HandleMessageService handleMessageService;

    public BotProcessUpdate(HandleQueryService handleQueryService, HandleMessageService handleMessageService){
        this.handleQueryService = handleQueryService;
        this.handleMessageService = handleMessageService;
    }

    public BotApiMethod handleUpdate(Update update) {
        if(update.hasCallbackQuery()){
            String[] splitedCallbackQuery = update.getCallbackQuery().getData().split(":");

            ButtonTypeEnum type = ButtonTypeEnum.valueOf(splitedCallbackQuery[0]);
            String query = null;
            if(splitedCallbackQuery.length == 2) {
                query = splitedCallbackQuery[1];
            }

            return handleQueryService.handleCallbackQuery(update.getCallbackQuery().getMessage().getChatId(), type, query, update.getCallbackQuery().getMessage().getMessageId());
        } else if(update.getMessage() != null && update.getMessage().hasText()){
            return handleMessageService.handleMessage(update.getMessage().getChatId(), update.getMessage().getText());
        }
        return null;
    }
}


