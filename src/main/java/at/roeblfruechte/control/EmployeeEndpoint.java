package at.roeblfruechte.control;

import at.roeblfruechte.boundary.EmployeeRepository;
import at.roeblfruechte.model.Employee;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/api/employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class EmployeeEndpoint {

    @Inject
    EmployeeRepository employeeRepository;

    @GET
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAllEmployees();
    }

    @GET
    @Path("{id}")
    public Employee getEmployeeById(@PathParam("id")Long id){
        return employeeRepository.findEmployeeById(id);
    }

    @GET
    @Path("{name}")
    public Employee getEmployeeByName(@PathParam("name")String name){
        return employeeRepository.findEmployeeByName(name);
    }

    @POST
    public Response createEmployee(@Context UriInfo info, Employee employee){
        if(employee == null) return Response.noContent().build();
        Employee newEmployee = new Employee();
        //If 1-* Relationship, persist here
        newEmployee.CopyProperties(employee);
        Employee savedEmployee = employeeRepository.persistEmployee(newEmployee);
        URI uri = info.getAbsolutePathBuilder().path("/" + savedEmployee.id).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteEmployee(@PathParam("id") Long id) {
        try {
            employeeRepository.deleteEmployee(id);
        } catch (EntityNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "Employee with id " + id + " does not exist")
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") Long id, Employee employee) {
        Employee employeeToUpdate = employeeRepository.updateEmployee(id, employee);
        if (employeeToUpdate == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "Employee with id " + id + " does not exist")
                    .build();
        } else {
            employeeRepository.updateEmployee(id,employee);
            return Response.ok().build();
        }
    }
}
