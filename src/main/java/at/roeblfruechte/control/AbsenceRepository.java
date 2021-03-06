package at.roeblfruechte.control;

import at.roeblfruechte.model.Absence;
import at.roeblfruechte.model.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class AbsenceRepository implements PanacheRepositoryBase<Absence, Long> {

    @Inject
    EmployeeRepository employeeRepository;

    public Absence findAbsenceById(Long id){
        Optional<Absence>  optionalAbsence = Absence.findByIdOptional(id);
        return optionalAbsence.orElseThrow(NotFoundException::new);
    }

    public List<Absence> findAbsencesByEmployee(Long employeeId){
        Employee employee = employeeRepository.findEmployeeById(employeeId);
        return list("employee_id" ,employeeId);
    }

    public Absence persistAbsence(Absence absence){return this.getEntityManager().merge(absence);}

    public Absence updateAbsence(Long id, Absence absence){
        Absence updateAbsence = findAbsenceById(id);
        updateAbsence.CopyProperties(absence);
        //if any relations get added to the model Absence, then we need to null check them
        return persistAbsence(updateAbsence);
    }

    public void deleteAbsence(Long id){ delete(findAbsenceById(id));}
}
