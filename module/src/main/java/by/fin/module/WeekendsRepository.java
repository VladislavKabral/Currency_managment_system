package by.fin.module;


import by.fin.module.entity.Weekend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WeekendsRepository extends JpaRepository<Weekend, Long> {

    Weekend findByCalendarDate(Date date);
}
