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
    LocalDate scheduleDate = LocalDate.now();

    @OneToMany(mappedBy = "workSchedule", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<WorkScheduling> workSchedulingList = new ArrayList<>();


    @Override
    public void CopyProperties(WorkSchedule other) {
        if(other != null){
            this.scheduleDate = other.scheduleDate;
            this.workSchedulingList = other.workSchedulingList;
        }
    }
}
