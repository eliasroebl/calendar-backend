package at.roeblfruechte.control;

import at.roeblfruechte.model.WorkSchedule;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@ApplicationScoped
public class WorkScheduleRepository implements PanacheRepositoryBase<WorkSchedule, Long>, Serializable {

    public List<WorkSchedule> findAllWorkSchedules() {
        List<WorkSchedule> list = WorkSchedule.listAll();
        list.forEach(workSchedule -> {
            //workSchedule.getWorkSchedulingList();
        });
        return list;
    }

    public WorkSchedule findWorkScheduleById(Long id){
        Optional<WorkSchedule> optionalWorkSchedule = WorkSchedule.findByIdOptional(id);
        optionalWorkSchedule.ifPresent(
                workSchedule -> {
                    //workSchedule.getWorkSchedulingList();
                }
        );
        return optionalWorkSchedule.orElseThrow(NotFoundException::new);
    }

    public List<WorkSchedule> findCurrentWeekWorkSchedules(LocalDate date){
        LocalDate dateMonday = date.minusDays((date.getDayOfWeek().getValue()) - 1);
        LocalDate dateSaturday = date.plusDays(5);
        return WorkSchedule.findAll().list().stream()
                .map(peb -> (WorkSchedule)peb)
                .filter(ws ->
                        ws.getScheduleDate().isAfter(dateMonday.minusDays(1)) &&
                        ws.getScheduleDate().isBefore(dateSaturday.plusDays(1)))
                .collect(Collectors.toList());
    }

    public WorkSchedule persistWorkSchedule(WorkSchedule workSchedule){
        return this.getEntityManager().merge(workSchedule);
    }

    public WorkSchedule updateWorkSchedule(Long id, WorkSchedule workSchedule){
        WorkSchedule updateWorkSchedule = findWorkScheduleById(id);
        updateWorkSchedule.CopyProperties(workSchedule);
        return persistWorkSchedule(updateWorkSchedule);
    }

    public void deleteWorkSchedule(Long id){ delete(findWorkScheduleById(id));}
}
