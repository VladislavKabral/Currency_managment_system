package by.fin.service;

import by.fin.module.entity.CurrencyRate;

import java.util.List;

public interface CurrencyRateService {

    List<CurrencyRate> findAll();

    List<CurrencyRate> findByCurrencyRateByCurrencyType(String currencyType);

    void saveCurrencyRate(CurrencyRate currencyRate);
}
