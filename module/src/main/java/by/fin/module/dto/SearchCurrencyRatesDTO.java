package by.fin.module.dto;

import lombok.Data;


@Data
public class SearchCurrencyRatesDTO {

    private String currencyType;

    private String startDate;

    private String endDate;
}
