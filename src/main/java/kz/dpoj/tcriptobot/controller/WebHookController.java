package kz.dpoj.tcriptobot.controller;

import kz.dpoj.tcriptobot.service.CriptoBot;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private CriptoBot criptoBot;

    public WebHookController(CriptoBot criptoBot){
        this.criptoBot = criptoBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return criptoBot.onWebhookUpdateReceived(update);
    }
}
