package by.fin.module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CurrencyRateDTO {

    @JsonProperty("Cur_ID")
    private Long currencyId;

    @JsonProperty("Date")
    private Date currencyDate;

    @JsonProperty("Cur_OfficialRate")
    private Double currencyRate;
}
