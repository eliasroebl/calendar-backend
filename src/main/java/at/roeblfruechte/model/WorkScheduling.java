package at.roeblfruechte.model;

import at.roeblfruechte.contracts.ELocation;
import at.roeblfruechte.contracts.ICopyable;
import com.fasterxml.jackson.annotation.JacksonAnnotation;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "WORK_SCHEDULING", schema = "CALENDAR")
public class WorkScheduling extends PanacheEntityBase implements ICopyable<WorkScheduling>, Serializable {

    @Id
    @SequenceGenerator(
            name ="workSchedulingSequence",
            sequenceName = "work_scheduling_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "workSchedulingSequence"
    )
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")
    public Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_SCHEDULE_ID", referencedColumnName = "ID")
    public WorkSchedule workSchedule;

    @Column(name = "FROM_DATE")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    public LocalDate from = LocalDate.now();

    @Column(name = "TO_DATE")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    public LocalDate to = LocalDate.now();

    @Column(name="LOCATION")
    public ELocation location;

    @Override
    public void CopyProperties(WorkScheduling other) {
        if(other != null){
            this.employee = other.employee;
            this.workSchedule = other.workSchedule;
            this.from = other.from;
            this.to = other.to;
            this.location = other.location;
        }
    }
}
