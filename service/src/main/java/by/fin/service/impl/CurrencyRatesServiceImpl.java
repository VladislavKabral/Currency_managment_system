package by.fin.service.impl;

import by.fin.module.CurrencyRatesRepository;
import by.fin.module.dto.CurrencyRateDTO;
import by.fin.module.entity.CurrencyRate;
import by.fin.module.entity.Weekend;
import by.fin.service.CurrencyRateService;
import by.fin.service.WeekendService;
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
    public List<CurrencyRate> findByCurrencyRateByCurrencyType(String currencyType) {
        return currencyRatesRepository.findByCurrencyType(currencyType);
    }

    @Override
    public void saveCurrencyRate(CurrencyRate currencyRate) {
        currencyRatesRepository.save(currencyRate);
    }

    public List<CurrencyRateDTO> getCurrencyRatesFromAPI(String currencyType, String startDate, String endDate) throws JsonProcessingException {
        String url = "https://api.nbrb.by/ExRates/Rates/Dynamics/" +
                currencyTypes.get(currencyType) +
                "?startDate=" +
                startDate +
                "&endDate=" +
                endDate;

        String json = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<CurrencyRateDTO> currencyRateDTOList;
        currencyRateDTOList = Arrays.asList(mapper.readValue(json, CurrencyRateDTO[].class));

        for (CurrencyRateDTO currencyRateDTO: currencyRateDTOList) {
            CurrencyRate currencyRate = new CurrencyRate();
            currencyRate.setCurrencyDate(currencyRateDTO.getDate());
            currencyRate.setCurrencyType(currencyType);
            currencyRate.setCurrencyRate(currencyRateDTO.getCur_OfficialRate());

            Weekend weekend = weekendsService.findByCalendarDate(currencyRateDTO.getDate());
            currencyRate.setCurrencyIsDayOff(weekend.isDayOff());

            saveCurrencyRate(currencyRate);
        }

        return currencyRateDTOList;
    }
}
