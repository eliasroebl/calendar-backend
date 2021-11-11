package at.roeblfruechte.model;

import at.roeblfruechte.contracts.AbsenceType;
import at.roeblfruechte.contracts.ICopyable;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "ABSENCE", schema = "CALENDAR")
public class Absence extends PanacheEntityBase implements ICopyable<Absence>, Serializable {

    @Id
    @SequenceGenerator(
            name ="absenceSequence",
            sequenceName = "absence_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "absenceSequence"
    )
    public Long id;

    @Column(name = "DATE_START")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    public LocalDate dateStart = LocalDate.now();

    @Column(name = "DATE_END")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    public LocalDate dateEnd = LocalDate.now();

    @Column(name = "ABSENCE_TYPE")
    public AbsenceType absenceType = AbsenceType.Krankenstand;

    @Column(name = "TEXT")
    public String text = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")
    public Employee employee;

    @Override
    public void CopyProperties(Absence other) {
        if(other != null) {
            this.dateStart = other.dateStart;
            this.dateEnd = other.dateEnd;
            this.absenceType = other.absenceType;
            this.text = other.text;
            this.employee = other.employee;
        }
    }
}
