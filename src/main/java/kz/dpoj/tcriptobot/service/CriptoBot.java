package kz.dpoj.tcriptobot.service;

import kz.dpoj.tcriptobot.buttons.InlineKeyboardMaker;
import kz.dpoj.tcriptobot.config.BotConfig;
import kz.dpoj.tcriptobot.model.ScheduleMessage;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.UUID;

@Component
public class CriptoBot extends TelegramWebhookBot {
    final BotConfig botConfig;
    final BotProcessUpdate botProcessUpdate;
    final InlineKeyboardMaker inlineKeyboardMaker;
    private TaskSchedulingService taskSchedulingService;
    private HandleQueryService handleQueryService;

    public CriptoBot(BotConfig botConfig, BotProcessUpdate botProcessUpdate, InlineKeyboardMaker inlineKeyboardMaker,
                     TaskSchedulingService taskSchedulingService, HandleQueryService handleQueryService){
        this.botConfig = botConfig;
        this.botProcessUpdate = botProcessUpdate;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
        this.handleQueryService = handleQueryService;
        this.taskSchedulingService = taskSchedulingService;
    }

    @SneakyThrows
    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        BotApiMethod msg = botProcessUpdate.handleUpdate(update);

        if (msg instanceof ScheduleMessage) {
            taskSchedulingService.scheduleATask(UUID.randomUUID().toString(), () -> {
                Long chatId = Long.parseLong(((ScheduleMessage) msg).getChatId());
                try {
                    execute(handleQueryService.handlePair(chatId, ((ScheduleMessage) msg).getPair()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }, "*/10 * * * * *");
            return msg;
        }

        return msg;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotPath() {
        return botConfig.getPath();
    }
}
