package kz.dpoj.tcriptobot.model.birja.exmo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ExmoData {
    private String quantity;
    private String amount;
    @JsonProperty(value = "avg_price")
    private String avgPrice;
}
