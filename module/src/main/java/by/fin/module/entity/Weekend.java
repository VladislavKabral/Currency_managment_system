package by.fin.module.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weekends")
public class Weekend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekend_id")
    private Long weekendId;

    @Column(name = "calendar_date")
    private Date calendarDate;

    @Column(name = "is_day_off")
    private boolean isDayOff;
}
