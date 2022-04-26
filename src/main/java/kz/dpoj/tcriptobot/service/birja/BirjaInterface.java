package kz.dpoj.tcriptobot.service.birja;

import java.util.List;

public interface BirjaInterface {
    String getDataByPair(String pair);

    List<String> getPairs();

    List<String> findByCurrency(String currency);

    List<String> getCandlesHistory(String pair, String from, String to, String resolution);
}


