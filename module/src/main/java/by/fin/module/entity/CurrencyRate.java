package by.fin.module.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "currency_rates")
public class CurrencyRate {

    @Id
    @Column(name = "currency_rate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long currencyId;

    @Column(name = "currency_date")
    private Date currencyDate;

    @Column(name = "currency_type")
    private String currencyType;

    @Column(name = "currency_rate")
    private Double currencyRate;

    @Column(name = "currency_is_day_off")
    private boolean currencyIsDayOff;
}
