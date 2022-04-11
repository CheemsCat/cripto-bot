package kz.dpoj.tcriptobot.service;

import kz.dpoj.tcriptobot.model.ExmoData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class BirjaService {
    private final WebClient webClient;

    public BirjaService(WebClient webClient){
        this.webClient = webClient;
    }

    public ExmoData getDataByPair(String pair){
        return webClient
                .get()
                .uri("/required_amount?pair=" + pair + "&quantity=1")
                .retrieve()
                .bodyToMono(ExmoData.class)
                .block();
    }

    public List<String> getPairs(){
        List<String> pairs = new ArrayList<>();

        Flux<Map<String,Object>> pairSettings =  webClient
                .get()
                .uri("/pair_settings")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<>() {});
        pairSettings.toStream().forEach(stringObjectMap -> pairs.addAll(stringObjectMap.keySet()));

        return pairs;
    }
}
