package kz.dpoj.tcriptobot.model.birja.exmo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CandlesData {
    @JsonProperty(value = "candles")
    private List<CandleData> candlesData;
}
