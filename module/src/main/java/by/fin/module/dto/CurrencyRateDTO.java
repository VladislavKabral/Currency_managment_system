package by.fin.module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CurrencyRateDTO {

    @JsonProperty("currency_rate_id")
    private Long currencyId;

    @JsonProperty("currency_date")
    private Date currencyDate;

    @JsonProperty("currency_rate")
    private Double currencyRate;
}
