package by.fin.web.controller;

import by.fin.module.dto.CurrencyRateDTO;
import by.fin.module.dto.SearchCurrencyRatesDTO;
import by.fin.module.entity.CurrencyRate;
import by.fin.service.CurrencyRateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/currency")
public class Currency {

    private final CurrencyRateService currencyRatesService;

    private final ModelMapper modelMapper;

    @Autowired
    public Currency(CurrencyRateService currencyRateService, ModelMapper modelMapper) {
        this.currencyRatesService = currencyRateService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{currencyType}")
    public List<CurrencyRateDTO> getCurrencyByType(@PathVariable("currencyType") String currencyType) {
        return currencyRatesService.findByCurrencyRateByCurrencyType(currencyType)
                .stream()
                .map(this::convertToCurrencyRateDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/addCurrencyToDB")
    public List<CurrencyRateDTO> addCurrencyToDB(@RequestBody SearchCurrencyRatesDTO searchCurrencyRatesDTO) {
        return currencyRatesService.getCurrencyRatesFromAPI(searchCurrencyRatesDTO.getCurrencyType(),
                searchCurrencyRatesDTO.getStartDate(),
                searchCurrencyRatesDTO.getEndDate());
    }

    private CurrencyRateDTO convertToCurrencyRateDTO(CurrencyRate currencyRate) {
        return modelMapper.map(currencyRate, CurrencyRateDTO.class);
    }
}
