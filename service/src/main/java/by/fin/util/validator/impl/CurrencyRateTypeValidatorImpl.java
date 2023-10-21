package by.fin.util.validator.impl;

import by.fin.util.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CurrencyRateTypeValidatorImpl implements Validator {

    private final List<String> currencyTypes = new ArrayList<>(){{
        addAll(Arrays.asList("USD", "EUR", "RUR", "UAH", "KZT", "CAD"));
    }};

    @Override
    public boolean validate(String data) {
        return currencyTypes.contains(data);
    }
}
