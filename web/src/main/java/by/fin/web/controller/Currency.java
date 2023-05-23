package by.fin.web.controller;

import by.fin.module.dto.CurrencyRateDTO;
import by.fin.module.dto.CurrencyTypeDTO;
import by.fin.module.dto.SearchCurrencyRatesDTO;
import by.fin.module.entity.CurrencyRate;
import by.fin.service.impl.CurrencyRatesServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/currency")
public class Currency {

    private final CurrencyRatesServiceImpl currencyRatesService;

    @Autowired
    public Currency(CurrencyRatesServiceImpl currencyRatesService) {
        this.currencyRatesService = currencyRatesService;
    }

    @GetMapping("/addRates")
    public String addRatesPage(@ModelAttribute("searchCurrencyRate") SearchCurrencyRatesDTO searchCurrencyRatesDTO) {
        return "addRatesPage";
    }

    @GetMapping("/getRates")
    public String getRatesPage(@ModelAttribute("currencyTypeDTO") CurrencyTypeDTO currencyTypeDTO) {
        return "getRatesPage";
    }

    @PostMapping("/getCurrencyRates")
    public String getRates(@ModelAttribute("currencyTypeDTO") CurrencyTypeDTO currencyTypeDTO, Model model) {
        List<CurrencyRate> currencyRates = currencyRatesService
                .findByCurrencyRateByCurrencyType(currencyTypeDTO.getCurrencyType());

        model.addAttribute("rates", currencyRates);

        return "redirect:/currency/getAddedRates";
    }

    @GetMapping("/getAddedRates")
    public String getAddedCurrencyRatesPage(@ModelAttribute("rates") List<CurrencyRate> currencyRates) {
        return "addedCurrencyRates";
    }

    @PostMapping("/addRates")
    public String addRates(@ModelAttribute("searchCurrencyRate") @Valid SearchCurrencyRatesDTO searchCurrencyRatesDTO,
                           Model model) throws JsonProcessingException {

        List<CurrencyRateDTO> res = currencyRatesService.getCurrencyRatesFromAPI(searchCurrencyRatesDTO.getCurrencyType(),
                searchCurrencyRatesDTO.getStartDate(), searchCurrencyRatesDTO.getEndDate());

        model.addAttribute("addedCurrencyRates", res);

        return "redirect:/";
    }
}
