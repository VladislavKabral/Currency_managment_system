package by.fin.module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CurrencyRateDTO {

    @JsonProperty("Cur_ID")
    private Long Cur_ID;

    @JsonProperty("Date")
    private Date Date;

    @JsonProperty("Cur_OfficialRate")
    private Double Cur_OfficialRate;
}
