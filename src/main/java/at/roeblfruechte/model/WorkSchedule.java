package at.roeblfruechte.model;

import at.roeblfruechte.contracts.ICopyable;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import net.bytebuddy.asm.Advice;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WORK_SCHEDULE", schema = "CALENDAR")
public class WorkSchedule extends PanacheEntityBase implements ICopyable<WorkSchedule>, Serializable {

    @Id
    @SequenceGenerator(
            name ="workScheduleSequence",
            sequenceName = "work_schedule_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "workScheduleSequence"
    )
    public Long id;

    @Column(name = "SCHEDULE_DATE")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    LocalDate scheduleDate;

    public WorkSchedule() {
    }

    public WorkSchedule(Long id, LocalDate scheduleDate, List<WorkScheduling> workSchedulingList) {
        this.id = id;
        setScheduleDate(scheduleDate);
        this.workSchedulingList = workSchedulingList;
    }

    @OneToMany(mappedBy = "workSchedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<WorkScheduling> workSchedulingList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
        if(scheduleDate == null){
            this.scheduleDate = LocalDate.now();
        }
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public List<WorkScheduling> getWorkSchedulingList() {
        return workSchedulingList;
    }

    @Override
    public void CopyProperties(WorkSchedule other) {
        if(other != null){
            this.scheduleDate = other.scheduleDate;
            this.workSchedulingList = other.workSchedulingList;
        }
    }
}
