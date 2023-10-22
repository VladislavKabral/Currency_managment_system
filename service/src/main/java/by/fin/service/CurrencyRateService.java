package by.fin.service;

import by.fin.module.dto.CurrencyRateDTO;
import by.fin.module.entity.CurrencyRate;
import by.fin.module.exception.CurrencyRateException;
import by.fin.module.exception.DateException;

import java.util.List;

public interface CurrencyRateService {

    List<CurrencyRate> findAll();

    List<CurrencyRate> findByCurrencyRateByCurrencyType(String currencyType) throws CurrencyRateException;

    List<CurrencyRateDTO> getCurrencyRatesFromAPI(String currencyType, String startDate, String endDate)
            throws DateException, CurrencyRateException;

    void saveCurrencyRate(CurrencyRate currencyRate);

    Double calculateAverageCurrencyRate(String currencyType, int monthNumber) throws CurrencyRateException;
}
