package at.roeblfruechte.control;

import at.roeblfruechte.model.Employee;
import at.roeblfruechte.model.WorkScheduling;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class WorkSchedulingRepository implements PanacheRepositoryBase<WorkScheduling, Long> {

    @Inject
    EmployeeRepository employeeRepository;

    public List<WorkScheduling> findAllWorkSchedulings() {
        return WorkScheduling.listAll();
    }

    public WorkScheduling findWorkSchedulingById(Long id){
        Optional<WorkScheduling> optionalWorkScheduling = WorkScheduling.findByIdOptional(id);
        return optionalWorkScheduling.orElseThrow(NotFoundException::new);
    }

    public List<WorkScheduling> findWorkSchedulingsByEmployee(Long employeeId){
        Employee employee = employeeRepository.findEmployeeById(employeeId);
        return list("employee_id", employee.id);
    }

    public WorkScheduling persistWorkScheduling(WorkScheduling workScheduling){return this.getEntityManager().merge(workScheduling);}

    public WorkScheduling updateWorkScheduling(Long id, WorkScheduling workScheduling){
        WorkScheduling updateWorkScheduling = findWorkSchedulingById(id);
        updateWorkScheduling.CopyProperties(workScheduling);
        return persistWorkScheduling(updateWorkScheduling);
    }

    public void deleteWorkScheduling(Long id){ delete(findWorkSchedulingById(id));}
}
