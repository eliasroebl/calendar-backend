package at.roeblfruechte.control;

import at.roeblfruechte.model.Employee;
import at.roeblfruechte.model.WorkSchedule;
import at.roeblfruechte.model.WorkScheduling;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@ApplicationScoped
public class WorkSchedulingRepository implements PanacheRepositoryBase<WorkScheduling, Long> {

    @Inject
    EmployeeRepository employeeRepository;

    public List<WorkScheduling> findAllWorkSchedulings() {

        List<WorkScheduling> workSchedulings = WorkScheduling.listAll();
        workSchedulings.forEach(
                workScheduling -> {
                    workScheduling.getScheduleDate();
                    workScheduling.getEmployee();
                }
        );
        return workSchedulings;
    }

    public WorkScheduling findWorkSchedulingById(Long id){
        Optional<WorkScheduling> optionalWorkScheduling = WorkScheduling.findByIdOptional(id);
        return optionalWorkScheduling.orElseThrow(NotFoundException::new);
    }

    public List<WorkScheduling> findWorkSchedulingsByEmployee(Long employeeId){
        Employee employee = employeeRepository.findEmployeeById(employeeId);
        List<WorkScheduling> workSchedulings = WorkSchedule.list("employee_id", employee.id);
        workSchedulings.forEach(
                workScheduling -> {
                    workScheduling.getScheduleDate();
                    workScheduling.getEmployee();
                }
        );
        return workSchedulings;
    }

    public List<WorkScheduling> findWorkSchedulingsByDate(LocalDate date){
        List<WorkScheduling> workSchedulings = WorkSchedule.list("schedule_date", date);
        workSchedulings.forEach(
                workScheduling -> {
                    workScheduling.getScheduleDate();
                    workScheduling.getEmployee();
                }
        );
        return workSchedulings;
    }

    public WorkScheduling persistWorkScheduling(WorkScheduling workScheduling){
        return this.getEntityManager().merge(workScheduling);}

    public WorkScheduling updateWorkScheduling(Long id, WorkScheduling workScheduling){
        WorkScheduling updateWorkScheduling = findWorkSchedulingById(id);
        updateWorkScheduling.CopyProperties(workScheduling);
        return persistWorkScheduling(updateWorkScheduling);
    }

    public void deleteWorkScheduling(Long id){ delete(findWorkSchedulingById(id));}
}
