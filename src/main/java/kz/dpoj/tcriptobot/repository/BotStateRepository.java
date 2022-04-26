package kz.dpoj.tcriptobot.repository;

import kz.dpoj.tcriptobot.model.BotState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotStateRepository extends MongoRepository<BotState, String> {
    BotState findByChatId(Long chatId);
}


