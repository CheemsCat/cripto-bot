package kz.dpoj.tcriptobot.service;

import kz.dpoj.tcriptobot.enumerate.ButtonNameEnum;
import kz.dpoj.tcriptobot.enumerate.ButtonTypeEnum;
import kz.dpoj.tcriptobot.keyboard.InlineKeyboardMaker;
import kz.dpoj.tcriptobot.enumerate.BirjaNameEnum;
import kz.dpoj.tcriptobot.model.*;
import kz.dpoj.tcriptobot.model.birja.ChartMessage;
import kz.dpoj.tcriptobot.repository.BotStateRepository;
import kz.dpoj.tcriptobot.repository.SubscriptionRepository;
import kz.dpoj.tcriptobot.service.birja.BirjaFabrica;
import kz.dpoj.tcriptobot.service.birja.BirjaInterface;
import kz.dpoj.tcriptobot.service.birja.ExmoBirjaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HandleQueryService {
    private InlineKeyboardMaker inlineKeyboardMaker;
    private SendMessageBuilder sendMessageBuilder;

    private BotStateRepository botStateRepository;

    private BirjaFabrica birjaFabrica;

    private SubscriptionRepository subscriptionRepository;

    private TaskSchedulingService taskSchedulingService;

    private ChartService chartService;

    @Value("${message.cron}")
    private String cronMessage;

    public HandleQueryService(InlineKeyboardMaker inlineKeyboardMaker, TaskSchedulingService taskSchedulingService,
                              SendMessageBuilder sendMessageBuilder, BotStateRepository botStateRepository,
                              BirjaFabrica birjaFabrica, SubscriptionRepository subscriptionRepository,
                              ChartService chartService){
        this.inlineKeyboardMaker = inlineKeyboardMaker;
        this.sendMessageBuilder = sendMessageBuilder;
        this.botStateRepository = botStateRepository;
        this.birjaFabrica = birjaFabrica;
        this.subscriptionRepository = subscriptionRepository;
        this.taskSchedulingService = taskSchedulingService;
        this.chartService = chartService;
    }

    public BotApiMethod handleCallbackQuery(Long chatId, ButtonTypeEnum type, String query, Integer messageId) {
        BotState botState = botStateRepository.findByChatId(chatId);
        switch (type) {
            case PAGE:
                return handlePage(chatId, query, messageId);
            case PAIR:
                if(botState.getState().equals(ButtonNameEnum.ADD_SUBSCRIBE.toString())){
                    botState.setPair(query);
                    botStateRepository.save(botState);
                    return sendMessageBuilder.buildSendMessage(chatId, cronMessage);
                } else if(botState.getState().equals(ButtonNameEnum.GET_CHART.toString())){
                    botState.setChartPair(query);
                    botStateRepository.save(botState);
                    return sendMessageBuilder.buildSendMessage(chatId, "Выберите период", inlineKeyboardMaker.getPeriodKeyboard());
//                    return chartService.getChartByPair(query, chatId);
                }else{
                    return handlePair(chatId, query);
                }
            case BIRJA:
                return handleBirja(chatId, query);
            case FIND:
                botState.setState(ButtonTypeEnum.FIND.toString());
                botStateRepository.save(botState);
                return sendMessageBuilder.buildSendMessage(chatId, "Введите валюту");
            case DELETE:
                taskSchedulingService.removeScheduledTask(query);
                subscriptionRepository.deleteByCronId(query);
                return sendMessageBuilder.buildSendMessage(chatId, "Подписка удалена");
            case CHART:
                return chartService.getChartByPair(query, chatId, botState.getChartPair());
        }

        return null;
    }

    public SendMessage handleBirja(Long chatId, String birja) {
        BotState botState = botStateRepository.findByChatId(chatId);
        if(botState.getState().equals(ButtonNameEnum.ADD_SUBSCRIBE.toString()) || botState.getState().equals(ButtonNameEnum.GET_CHART.toString())) {
            botState.setBirjaName(birja);
            botStateRepository.save(botState);
        }

        BirjaInterface birjaType = birjaFabrica.getBirjaByName(birja);
        List<String> pairs  = birjaType.getPairs();
        List<String> sortedPairs = pairs.stream().sorted().toList();
        return sendMessageBuilder.buildSendMessage(chatId, "Выберите пару", inlineKeyboardMaker.getPairKeyboard(sortedPairs, 1));
    }

    public SendMessage handlePair(Long chatId, String pair) {
        BirjaInterface birjaType = birjaFabrica.getBirjaByChatId(chatId);

        String data = birjaType.getDataByPair(pair);
        return sendMessageBuilder.buildSendMessage(chatId, "Цена " + pair + " - " + data);
    }

    private EditMessageReplyMarkup handlePage(Long chatId, String pageNumber, Integer messageId){
        BotState botState = botStateRepository.findByChatId(chatId);
        BirjaInterface birjaType = birjaFabrica.getBirjaByChatId(chatId);
        List<String> pairs = new ArrayList<>();

        if(botState.getState().equals(ButtonTypeEnum.FIND.toString()) && botState.getFindPair() != null)
            pairs = birjaType.findByCurrency(botState.getFindPair());
        else
            pairs = birjaType.getPairs();
        return new EditMessageReplyMarkup(chatId.toString(), messageId, null, (InlineKeyboardMarkup) inlineKeyboardMaker.getPairKeyboard(pairs, Integer.parseInt(pageNumber)));
    }
}
