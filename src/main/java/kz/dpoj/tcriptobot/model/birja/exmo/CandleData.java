package kz.dpoj.tcriptobot.model.birja.exmo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CandleData {
    @JsonProperty(value = "c")
    private String price;
}
