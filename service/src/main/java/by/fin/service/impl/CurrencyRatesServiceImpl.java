package by.fin.service.impl;

import by.fin.module.CurrencyRatesRepository;
import by.fin.module.dto.CurrencyRateDTO;
import by.fin.module.entity.CurrencyRate;
import by.fin.module.entity.Weekend;
import by.fin.module.exception.CurrencyRateException;
import by.fin.module.exception.DateException;
import by.fin.service.CurrencyRateService;
import by.fin.service.WeekendService;
import by.fin.util.validator.impl.CurrencyRateTypeValidatorImpl;
import by.fin.util.validator.impl.DateValidatorImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyRatesServiceImpl implements CurrencyRateService {

    private final CurrencyRatesRepository currencyRatesRepository;

    private final WeekendService weekendsService;

    private final RestTemplate restTemplate;

    private final DateValidatorImpl dateValidator;

    private final CurrencyRateTypeValidatorImpl currencyRateTypeValidator;

    private final Map<String, Integer> currencyTypes = new HashMap<>() {{
        put("USD", 145);
        put("EUR", 19);
        put("RUR", 141);
        put("UAH", 169);
        put("KZT", 174);
        put("CAD", 23);
    }};

    @Override
    public List<CurrencyRate> findAll() {
        return currencyRatesRepository.findAll();
    }

    @Override
    public List<CurrencyRate> findByCurrencyRateByCurrencyType(String currencyType) throws CurrencyRateException {
        if (!currencyRateTypeValidator.validate(currencyType)) {
            throw new CurrencyRateException("Invalid currency type");
        }

        return currencyRatesRepository.findByCurrencyType(currencyType);
    }

    public CurrencyRate findByCurrencyDateAndCurrencyType(Date date, String type) {
        return currencyRatesRepository.findByCurrencyDateAndCurrencyType(date, type);
    }

    @Override
    public void saveCurrencyRate(CurrencyRate currencyRate) {
        currencyRatesRepository.save(currencyRate);
    }

    public List<CurrencyRateDTO> getCurrencyRatesFromAPI(String currencyType, String startDate, String endDate)
            throws DateException, CurrencyRateException {

        if (!currencyRateTypeValidator.validate(currencyType)) {
            throw new CurrencyRateException("Invalid currency type");
        }

        if (!dateValidator.validate(startDate)) {
            throw new DateException("Invalid start date format");
        }

        if (!dateValidator.validate(endDate)) {
            throw new DateException("Invalid end date format");
        }

        String url = "https://api.nbrb.by/ExRates/Rates/Dynamics/" +
                currencyTypes.get(currencyType) +
                "?startDate=" +
                startDate +
                "&endDate=" +
                endDate;

        String json = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<CurrencyRateDTO> currencyRateDTOList = new ArrayList<>();
        try {
            currencyRateDTOList = Arrays.asList(mapper.readValue(json, CurrencyRateDTO[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        for (CurrencyRateDTO currencyRateDTO: currencyRateDTOList) {
            if (findByCurrencyDateAndCurrencyType(currencyRateDTO.getCurrencyDate(), currencyType) == null) {
                CurrencyRate currencyRate = new CurrencyRate();
                currencyRate.setCurrencyDate(currencyRateDTO.getCurrencyDate());
                currencyRate.setCurrencyType(currencyType);
                currencyRate.setCurrencyRate(currencyRateDTO.getCurrencyRate());

                Weekend weekend = weekendsService.findByCalendarDate(currencyRateDTO.getCurrencyDate());
                currencyRate.setCurrencyIsDayOff(weekend.isDayOff());

                saveCurrencyRate(currencyRate);
            }
        }

        return currencyRateDTOList;
    }

    private List<CurrencyRate> sortCurrencyRates(List<CurrencyRate> currencyRates, int monthNumber) {
        List<CurrencyRate> sortedCurrencyRates = new ArrayList<>();

        for (CurrencyRate currencyRate: currencyRates) {
            if (currencyRate.getCurrencyDate().getMonth() == monthNumber) {
                sortedCurrencyRates.add(currencyRate);
            }
        }

        return sortedCurrencyRates;
    }

    public Double calculateAverageCurrencyRate(String currencyType, int monthNumber) throws CurrencyRateException {
        List<CurrencyRate> currencyRates = findByCurrencyRateByCurrencyType(currencyType);
        List<CurrencyRate> sortedCurrencyRates = sortCurrencyRates(currencyRates, monthNumber - 1);

        double averageCurrencyRate = 1.0;
        int daysCount = 0;
        for (CurrencyRate sortedCurrencyRate : sortedCurrencyRates) {
            if (!sortedCurrencyRate.isCurrencyIsDayOff()) {
                daysCount++;
                averageCurrencyRate *= sortedCurrencyRate.getCurrencyRate();
            }
        }

        return Math.pow(averageCurrencyRate, 1.0 / daysCount);
    }
}
