package at.roeblfruechte.control;

import at.roeblfruechte.model.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class EmployeeRepository implements PanacheRepositoryBase<Employee, Long> {

    public List<Employee> findAllEmployees(){
        return Employee.listAll();
    }

    public Employee findEmployeeById(Long id){
        Optional<Employee> optionalEmployee = Employee.findByIdOptional(id);
        return optionalEmployee.orElseThrow(NotFoundException::new);
    }

    public Employee findEmployeeByName(String name){
        Optional<Employee> optionalEmployee = Employee.find("name", name).firstResultOptional();
        return optionalEmployee.orElseThrow(NotFoundException::new);
    }

    public Employee persistEmployee(Employee employee){return this.getEntityManager().merge(employee);}

    public Employee updateEmployee(Long id, Employee employee){
        Employee updateEmployee = findEmployeeById(id);
        updateEmployee.CopyProperties(employee);
        return persistEmployee(updateEmployee);
    }

    public void deleteEmployee(Long id){ delete(findEmployeeById(id));}
}
