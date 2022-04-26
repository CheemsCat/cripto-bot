package kz.dpoj.tcriptobot.service;

import com.image.charts.ImageCharts;
import kz.dpoj.tcriptobot.keyboard.InlineKeyboardMaker;
import kz.dpoj.tcriptobot.config.BotConfig;
import kz.dpoj.tcriptobot.model.ScheduleMessage;
import kz.dpoj.tcriptobot.model.birja.ChartMessage;
import kz.dpoj.tcriptobot.repository.SubscriptionRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class    CriptoBot extends TelegramWebhookBot {
    final BotConfig botConfig;
    final BotProcessUpdate botProcessUpdate;
    final InlineKeyboardMaker inlineKeyboardMaker;
    private TaskSchedulingService taskSchedulingService;
    private HandleQueryService handleQueryService;

    private SubscriptionRepository subscriptionRepository;

    public CriptoBot(BotConfig botConfig, BotProcessUpdate botProcessUpdate, InlineKeyboardMaker inlineKeyboardMaker,
                     TaskSchedulingService taskSchedulingService, HandleQueryService handleQueryService,
                     SubscriptionRepository subscriptionRepository){
        this.botConfig = botConfig;
        this.botProcessUpdate = botProcessUpdate;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
        this.handleQueryService = handleQueryService;
        this.taskSchedulingService = taskSchedulingService;
        this.subscriptionRepository = subscriptionRepository;
    }

    @SneakyThrows
    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        BotApiMethod msg = botProcessUpdate.handleUpdate(update);

        if (msg instanceof ScheduleMessage) {
            taskSchedulingService.scheduleATask(((ScheduleMessage) msg).getCronId(), () -> {
                Long chatId = Long.parseLong(((ScheduleMessage) msg).getChatId());
                try {
                    execute(handleQueryService.handlePair(chatId, ((ScheduleMessage) msg).getPair()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }, ((ScheduleMessage) msg).getCron());
            return msg;
        } else if(msg instanceof ChartMessage){
            execute(((ChartMessage) msg).getSendPhoto());
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
