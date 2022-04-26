package kz.dpoj.tcriptobot.repository;

import kz.dpoj.tcriptobot.model.BotState;
import kz.dpoj.tcriptobot.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    List<Subscription> findAllByChatId(Long chatId);

    void deleteByCronId(String cronId);
}


