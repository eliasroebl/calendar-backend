package at.roeblfruechte.boundary;

import at.roeblfruechte.model.WorkSchedule;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class WorkScheduleRepository implements PanacheRepositoryBase<WorkSchedule, Long>, Serializable {

    public List<WorkSchedule> findAllWorkSchedules() {
        List<WorkSchedule> list = WorkSchedule.listAll();
        list.forEach(workSchedule -> {
            workSchedule.getScheduleDate();
            workSchedule.getWorkSchedulingList();
        });
        return list;
    }

    public WorkSchedule findWorkScheduleById(Long id){
        Optional<WorkSchedule> optionalWorkSchedule = WorkSchedule.findByIdOptional(id);
        optionalWorkSchedule.stream().forEach(
                workSchedule -> {
                    workSchedule.getWorkSchedulingList();
                    workSchedule.getScheduleDate();
                }
        );
        return optionalWorkSchedule.orElseThrow(NotFoundException::new);
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
