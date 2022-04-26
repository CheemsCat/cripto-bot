package kz.dpoj.tcriptobot.service.birja;

import kz.dpoj.tcriptobot.model.birja.exmo.CandlesData;
import kz.dpoj.tcriptobot.model.birja.gate.GateData;
import kz.dpoj.tcriptobot.model.birja.gate.GatePairData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GateBirjaService implements BirjaInterface{
    @Value("${gate.baseurl}")
    private String baseUrl;

    private WebClient webClient;

    public GateBirjaService(WebClient webClient){
        this.webClient = webClient;
    }
    @Override
    public String getDataByPair(String pair) {
        List<GatePairData> exmoData = webClient.mutate().baseUrl(baseUrl).build()
                .get()
                .uri("/tickers?currency_pair=" + pair)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GatePairData>>() {})
                .block();
        return exmoData.get(0).getLast();
    }

    @Override
    public List<String> getPairs() {
        List<String> pairs = new ArrayList<>();

        List<GateData> pairSettings =  webClient.mutate().baseUrl(baseUrl).build()
                .get()
                .uri("/currency_pairs")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GateData>>() {})
                .block();

        pairSettings.stream().filter(pair -> pair.getId().length()<9).limit(200).forEach(pair -> pairs.add(pair.getId()));
        List<String> sortedPairs = pairs.stream().sorted().toList();

        return sortedPairs;
    }

    @Override
    public List<String> getCandlesHistory(String pair, String from, String to, String resolution) {
        List<String> prices = new ArrayList<>();
        System.out.println(pair + " " + from + " " + to + " " + resolution);
        List<List<String>> candlesData = webClient.mutate().baseUrl(baseUrl).build()
                .get()
                .uri("/candlesticks?currency_pair=" + pair + "&interval=" + resolution + "&from=" + from + "&to=" + to)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<List<String>>>() {})
                .block();
        if(candlesData != null)
            candlesData.stream().forEach(pr -> prices.add(pr.get(2)));
        return prices;
    }

    @Override
    public List<String> findByCurrency(String currency) {
        List<String> pairs = getPairs();
        return pairs.stream().filter(pair -> pair.contains(currency.toUpperCase())).collect(Collectors.toList());
    }
}

