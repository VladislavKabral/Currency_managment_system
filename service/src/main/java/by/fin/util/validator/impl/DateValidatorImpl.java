package by.fin.util.validator.impl;

import by.fin.util.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DateValidatorImpl implements Validator {

    private static final String DATE_PATTERN = "((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";

    private static final int PLACE_OF_DAY = 3;

    private static final int PLACE_OF_MONTH = 2;

    private static final int PLACE_OF_YEAR = 1;

    private final Pattern pattern;

    public DateValidatorImpl() {
        pattern = Pattern.compile(DATE_PATTERN);
    }


    @Override
    public boolean validate(String data) {
        Matcher matcher = pattern.matcher(data);

        if (matcher.matches()) {
            matcher.reset();

            if (matcher.find()) {
                String day = matcher.group(PLACE_OF_DAY);
                String month = matcher.group(PLACE_OF_MONTH);
                int year = Integer.parseInt(matcher.group(PLACE_OF_YEAR));

                if ("31".equals(day)) {
                    return Arrays.asList(new String[]{"1", "01", "3", "03", "5", "05",
                            "7", "07", "8", "08", "10", "12"})
                            .contains(month);
                } else if ("2".equals(month) || "02".equals(month)) {
                    if (year % 4 == 0) {
                        if (year % 100 == 0) {
                            if (year % 400 == 0) {
                                return Integer.parseInt(day) <= 29;
                            }

                            return Integer.parseInt(day) <= 28;
                        }

                        return Integer.parseInt(day) <= 29;
                    } else {
                        return Integer.parseInt(day) <= 28;
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }
}
