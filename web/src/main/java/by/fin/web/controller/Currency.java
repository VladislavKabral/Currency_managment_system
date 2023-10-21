package by.fin.web.controller;

import by.fin.module.dto.AverageCurrencyRateDTO;
import by.fin.module.dto.CurrencyRateDTO;
import by.fin.module.dto.ErrorResponseDTO;
import by.fin.module.dto.SearchCurrencyRatesDTO;
import by.fin.module.entity.CurrencyRate;
import by.fin.module.entity.ErrorResponse;
import by.fin.module.exception.DateException;
import by.fin.service.CurrencyRateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<List<CurrencyRateDTO>> getCurrencyByType(@PathVariable("currencyType") String currencyType) {
        List<CurrencyRateDTO> currencyRateDTOS = currencyRatesService.findByCurrencyRateByCurrencyType(currencyType)
            .stream()
            .map(this::convertToCurrencyRateDTO)
            .collect(Collectors.toList());

        return new ResponseEntity<>(currencyRateDTOS, HttpStatus.OK);
    }

    @PostMapping("/addCurrencyToDB")
    public ResponseEntity<List<CurrencyRateDTO>> addCurrencyToDB(@RequestBody SearchCurrencyRatesDTO searchCurrencyRatesDTO)
            throws DateException {
        List<CurrencyRateDTO> currencyRateDTOS = currencyRatesService.getCurrencyRatesFromAPI(
            searchCurrencyRatesDTO.getCurrencyType(),
            searchCurrencyRatesDTO.getStartDate(),
            searchCurrencyRatesDTO.getEndDate());

        return new ResponseEntity<>(currencyRateDTOS, HttpStatus.OK);
    }

    @GetMapping("/averageCurrencyRate/{currencyType}/{monthNumber}")
    public ResponseEntity<AverageCurrencyRateDTO> getAverageCurrencyRate(@PathVariable String currencyType,
                                                                         @PathVariable int monthNumber) {
        AverageCurrencyRateDTO averageCurrencyRateDTO = new AverageCurrencyRateDTO();

        averageCurrencyRateDTO.setRate(currencyRatesService.calculateAverageCurrencyRate(currencyType, monthNumber));

        return new ResponseEntity<>(averageCurrencyRateDTO, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleExceptionResponse(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(convertToErrorResponseDTO(errorResponse), HttpStatus.BAD_REQUEST);
    }

    private CurrencyRateDTO convertToCurrencyRateDTO(CurrencyRate currencyRate) {
        return modelMapper.map(currencyRate, CurrencyRateDTO.class);
    }

    private ErrorResponseDTO convertToErrorResponseDTO(ErrorResponse errorResponse) {
        return modelMapper.map(errorResponse, ErrorResponseDTO.class);
    }
}
