package kz.dpoj.tcriptobot.service;

import com.image.charts.ImageCharts;
import kz.dpoj.tcriptobot.enumerate.ButtonTypeEnum;
import kz.dpoj.tcriptobot.keyboard.InlineKeyboardMaker;
import kz.dpoj.tcriptobot.keyboard.ReplyKeyboardMaker;
import kz.dpoj.tcriptobot.model.BotState;
import kz.dpoj.tcriptobot.enumerate.ButtonNameEnum;
import kz.dpoj.tcriptobot.model.ScheduleMessage;
import kz.dpoj.tcriptobot.model.Subscription;
import kz.dpoj.tcriptobot.repository.BotStateRepository;
import kz.dpoj.tcriptobot.repository.SubscriptionRepository;
import kz.dpoj.tcriptobot.service.birja.BirjaFabrica;
import kz.dpoj.tcriptobot.service.birja.BirjaInterface;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HandleMessageService {
    private InlineKeyboardMaker inlineKeyboardMaker;
    private ReplyKeyboardMaker replyKeyboardMaker;
    private SendMessageBuilder sendMessageBuilder;

    private BotStateRepository botStateRepository;

    private SubscriptionRepository subscriptionRepository;
    private BirjaFabrica birjaFabrica;

    @Value("${message.helpbutton}")
    private String helpButton;

    @Value("${message.subscribe}")
    private String messageSubscribe;

    @Value("${message.notfoundsubscribe}")
    private String messageNotFoundSubscribe;

    @Value("${message.find}")
    private String messageFind;

    public HandleMessageService(InlineKeyboardMaker inlineKeyboardMaker, ReplyKeyboardMaker replyKeyboardMaker,
                                SendMessageBuilder sendMessageBuilder, BotStateRepository botStateRepository,
                                BirjaFabrica birjaFabrica, SubscriptionRepository subscriptionRepository){
        this.inlineKeyboardMaker = inlineKeyboardMaker;
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.sendMessageBuilder = sendMessageBuilder;
        this.botStateRepository = botStateRepository;
        this.birjaFabrica = birjaFabrica;
        this.subscriptionRepository = subscriptionRepository;
    }

    @SneakyThrows
    public SendMessage handleMessage(Long chatId, String messageText) {
        BotState botState = botStateRepository.findByChatId(chatId);
        if(messageText.equals(ButtonNameEnum.GET_PRICE.getButtonName())){
            saveBotState(ButtonNameEnum.GET_PRICE, chatId, botState);
            return sendMessageBuilder.buildSendMessage(chatId, "Выберите биржу", inlineKeyboardMaker.getKeyboard());
        } else if(messageText.equals(ButtonNameEnum.ADD_SUBSCRIBE.getButtonName())){
            saveBotState(ButtonNameEnum.ADD_SUBSCRIBE, chatId, botState);
            return sendMessageBuilder.buildSendMessage(chatId, "Выберите биржу", inlineKeyboardMaker.getKeyboard());
        } else if(messageText.equals(ButtonNameEnum.SHOW_SUBSCRIBE.getButtonName())){
            List<Subscription> subscriptions = subscriptionRepository.findAllByChatId(chatId);
            if(subscriptions.isEmpty()){
                return sendMessageBuilder.buildSendMessage(chatId, messageNotFoundSubscribe);
            }
            return sendMessageBuilder.buildSendMessage(chatId, messageSubscribe, inlineKeyboardMaker.getSubscriptionKeyboard(subscriptions));
        } else if(messageText.equals(ButtonNameEnum.HELP_BUTTON.getButtonName())){
            return sendMessageBuilder.buildSendMessage(chatId, helpButton);
        } else if(botState != null && botState.getState().equals(ButtonNameEnum.ADD_SUBSCRIBE.toString()) && botState.getPair() != null){
            String uuid = UUID.randomUUID().toString();
            String subPair = botState.getPair();
            saveSubscriptionAndUpdateBotState(chatId, subPair, botState, uuid);
            return new ScheduleMessage(chatId.toString(), "Вы подписались на пару " + subPair, subPair, messageText, uuid);
        } else if (botState != null && botState.isFind()) {
            List<String> pairs =  findPairs(botState, messageText);
            if(pairs.isEmpty())
                return sendMessageBuilder.buildSendMessage(chatId, messageFind);
            else
                return sendMessageBuilder.buildSendMessage(chatId, "Найдены следующие пары:", inlineKeyboardMaker.getPairKeyboard(pairs, 1));
        } else if(messageText.equals(ButtonNameEnum.GET_CHART.getButtonName())){
            saveBotState(ButtonNameEnum.GET_CHART, chatId, botState);
            return sendMessageBuilder.buildSendMessage(chatId, "Выберите биржу", inlineKeyboardMaker.getKeyboard());
        }else{
            return sendMessageBuilder.buildSendMessage(chatId, "Выберите кнопку!", replyKeyboardMaker.getKeyboard());
        }
    }

    private void saveBotState(ButtonNameEnum buttonNameEnum, Long chatId, BotState botState){
        if(botState == null) {
            BotState botSt = new BotState();
            botSt.setState(buttonNameEnum.toString());
            botSt.setChatId(chatId);
            botStateRepository.save(botSt);
        } else{
            botState.setState(buttonNameEnum.toString());
            botState.setFind(false);
            botState.setFindPair(null);
            botStateRepository.save(botState);
        }
    }

    private void saveSubscriptionAndUpdateBotState(Long chatId, String subPair, BotState botState, String uuid){
        subscriptionRepository.save(new Subscription(chatId, subPair, botState.getBirjaName(), uuid));
        botState.setPair(null);
        botStateRepository.save(botState);
    }

    private List<String> findPairs(BotState botState, String messageText){
        botState.setFindPair(messageText);
        botStateRepository.save(botState);
        BirjaInterface birjaType = birjaFabrica.getBirjaByName(botState.getBirjaName());
        return birjaType.findByCurrency(messageText);
    }
}
