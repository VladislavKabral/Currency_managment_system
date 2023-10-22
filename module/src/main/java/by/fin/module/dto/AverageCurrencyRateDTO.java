package by.fin.module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AverageCurrencyRateDTO {

    @JsonProperty("rate")
    private double rate;
}
