package kz.dpoj.tcriptobot.service.birja;

import kz.dpoj.tcriptobot.enumerate.BirjaNameEnum;
import kz.dpoj.tcriptobot.model.BotState;
import kz.dpoj.tcriptobot.repository.BotStateRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

@Component
public class BirjaFabrica {
    private ExmoBirjaService exmoBirjaService;
    private BotStateRepository botStateRepository;

    private GateBirjaService gateBirjaService;

    public BirjaFabrica(ExmoBirjaService exmoBirjaService, BotStateRepository botStateRepository,
                        GateBirjaService gateBirjaService){
        this.exmoBirjaService = exmoBirjaService;
        this.botStateRepository = botStateRepository;
        this.gateBirjaService = gateBirjaService;
    }

    public BirjaInterface getBirjaByChatId(Long chatId){
        BotState botState = botStateRepository.findByChatId(chatId);

        if(botState.getBirjaName().equals(BirjaNameEnum.EXMO.getButtonName())){
            return exmoBirjaService;
        } else{
            return gateBirjaService;
        }
    }

    public BirjaInterface getBirjaByName(String name){
        if(name.equals(BirjaNameEnum.EXMO.getButtonName())){
            return exmoBirjaService;
        } else{
            return gateBirjaService;
        }
    }
}
