package kz.dpoj.tcriptobot.service.birja;

import kz.dpoj.tcriptobot.model.birja.exmo.CandleData;
import kz.dpoj.tcriptobot.model.birja.exmo.CandlesData;
import kz.dpoj.tcriptobot.model.birja.exmo.ExmoData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExmoBirjaService implements BirjaInterface{
    @Value("${exmo.baseurl}")
    private String baseUrl;

    private WebClient webClient;

    public ExmoBirjaService(WebClient webClient){
        this.webClient = webClient;
    }

    @Override
    public String getDataByPair(String pair){
        ExmoData exmoData = webClient.mutate().baseUrl(baseUrl).build()
                .get()
                .uri("/required_amount?pair=" + pair + "&quantity=1")
                .retrieve()
                .bodyToMono(ExmoData.class)
                .block();
        return exmoData.getAmount();
    }

    @Override
    public List<String> getPairs(){
        List<String> pairs = new ArrayList<>();

        Flux<Map<String,Object>> pairSettings =  webClient.mutate().baseUrl(baseUrl).build()
                .get()
                .uri("/pair_settings")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<>() {});
        pairSettings.toStream().forEach(stringObjectMap -> pairs.addAll(stringObjectMap.keySet()));
        List<String> sortedPairs = pairs.stream().sorted().toList();

        return sortedPairs;
    }

    @Override
    public List<String> getCandlesHistory(String pair, String from, String to, String resolution) {
        List<String> prices = new ArrayList<>();
        System.out.println(pair + " " + from + " " + to + " " + resolution);
        CandlesData candlesData = webClient.mutate().baseUrl(baseUrl).build()
                .get()
                .uri("/candles_history?symbol=" + pair + "&resolution=" + resolution + "&from=" + from + "&to=" + to)
                .retrieve()
                .bodyToMono(CandlesData.class)
                .block();
        if(candlesData != null && candlesData.getCandlesData() != null)
            candlesData.getCandlesData().stream().forEach(pr -> prices.add(pr.getPrice()));
        return prices;
    }

    @Override
    public List<String> findByCurrency(String currency) {
        List<String> pairs = getPairs();
        return pairs.stream().filter(pair -> pair.contains(currency.toUpperCase())).collect(Collectors.toList());
    }
}

