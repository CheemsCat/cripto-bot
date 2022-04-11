//package kz.dpoj.tcriptobot.scheduled;
//
//import kz.dpoj.tcriptobot.service.CriptoBot;
//import kz.dpoj.tcriptobot.service.HandleQueryService;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
//
//import java.io.Serializable;
//
//@Component
//public class TaskDefinitionBean implements Runnable{
//    private String pair;
//    private Long chatId;
//    private Executor executor;
//    private HandleQueryService handleQueryService;
//
//    public TaskDefinitionBean(HandleQueryService handleQueryService, Executor executor){
//        this.handleQueryService = handleQueryService;
//        this.executor = executor;
//    }
//
//    @Override
//    public void run() {
//        handleQueryService.handlePair(chatId, pair);
//    }
//
//    public String getPair() {
//        return pair;
//    }
//
//    public void setPair(String pair) {
//        this.pair = pair;
//    }
//
//    public Long getChatId() {
//        return chatId;
//    }
//
//    public void setChatId(Long chatId) {
//        this.chatId = chatId;
//    }
//}
