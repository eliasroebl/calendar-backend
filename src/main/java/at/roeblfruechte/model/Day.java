package at.roeblfruechte.model;

import at.roeblfruechte.contracts.ICopyable;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "DAY", schema = "CALENDAR")
public class Day extends PanacheEntityBase implements ICopyable<Day>, Serializable {

    @Id
    @SequenceGenerator(
            name ="daySequence",
            sequenceName = "day_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "daySequence"
    )
    public Long id;

    @Column(name = "DATE")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    public LocalDate date = LocalDate.now();

    @Override
    public void CopyProperties(Day other) {
        if(other != null){
            this.date = other.date;
        }
    }
}
