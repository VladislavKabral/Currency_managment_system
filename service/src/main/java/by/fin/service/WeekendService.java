package by.fin.service;

import by.fin.module.entity.Weekend;

import java.util.Date;
import java.util.List;

public interface WeekendService {
    List<Weekend> findAll();

    Weekend findByCalendarDate(Date date);
}
