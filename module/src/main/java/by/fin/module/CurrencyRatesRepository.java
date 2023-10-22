package by.fin.module;

import by.fin.module.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CurrencyRatesRepository extends JpaRepository<CurrencyRate, Long> {

    List<CurrencyRate> findByCurrencyType(String currencyType);
    CurrencyRate findByCurrencyDateAndCurrencyType(Date date, String type);
}
