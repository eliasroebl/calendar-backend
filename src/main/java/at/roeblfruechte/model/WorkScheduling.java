package at.roeblfruechte.model;

import at.roeblfruechte.contracts.ELocation;
import at.roeblfruechte.contracts.ICopyable;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")
    public Employee employee;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_SCHEDULE_ID", referencedColumnName = "ID")
    public WorkSchedule workSchedule;*/

    @JsonbDateFormat("yyyy-MM-dd")
    @Column(name="SCHEDULE_DATE")
    public LocalDate scheduleDate;

    @JsonbDateFormat("HH:mm")
    @Column(name = "FROM_TIME")
    public LocalTime from;

    @JsonbDateFormat("HH:mm")
    @Column(name = "TO_TIME")
    public LocalTime to;

    @Column(name="LOCATION")
    public ELocation location;

    @Override
    public void CopyProperties(WorkScheduling other) {
        if(other != null){
            if(other.employee != null) this.employee = other.employee;
            //if(other.workSchedule != null) this.workSchedule = other.workSchedule;
            if(other.from != null) this.from = other.from;
            if(other.to != null) this.to = other.to;
            if(other.location != null) this.location = other.location;
            if(other.scheduleDate != null) this.scheduleDate = other.scheduleDate;
        }
    }

    public LocalDate getScheduleDate() {

        return scheduleDate;
    }

    public Employee getEmployee(){
        return employee;
    }

}
