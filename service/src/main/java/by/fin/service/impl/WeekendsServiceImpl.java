package by.fin.service.impl;

import by.fin.module.WeekendsRepository;
import by.fin.module.entity.Weekend;
import by.fin.service.WeekendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WeekendsServiceImpl implements WeekendService {
   private final WeekendsRepository weekendsRepository;

    @Override
    public List<Weekend> findAll() {
       return weekendsRepository.findAll();
    }

    @Override
    public Weekend findByCalendarDate(Date date) {
        return weekendsRepository.findByCalendarDate(date);
    }
}
